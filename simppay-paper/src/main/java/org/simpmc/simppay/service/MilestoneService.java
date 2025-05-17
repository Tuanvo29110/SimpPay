package org.simpmc.simppay.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MocNapConfig;
import org.simpmc.simppay.config.types.data.MilestoneConfig;
import org.simpmc.simppay.data.milestone.MilestoneType;
import org.simpmc.simppay.event.PlayerMilestoneEvent;
import org.simpmc.simppay.event.ServerMilestoneEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class MilestoneService {
    // NOTE: BossBar are static, changing bossbar reflect the changes to player who are added to it
    // Design a central bossbar of the plugin
    // Use one for player
    // one for entire server

    @Getter
    private BossBar serverbossBar;
    @Getter
    private final HashMap<UUID, BossBar> playerBossBar = new HashMap<>();
    
    // Map to store all milestone configs by amount
    private final Map<Integer, MilestoneConfig> allMilestoneConfigs = new TreeMap<>();
    
    // Map to store active milestones by type (only one milestone per type)
    @Getter
    private final Map<MilestoneType, MilestoneConfig> milestoneMap = new ConcurrentHashMap<>();
    
    // Set to track completed milestones to prevent re-triggering
    private final Set<MilestoneConfig> completedMilestones = Collections.synchronizedSet(new HashSet<>());

    public void loadMilestoneConfig() {
        MocNapConfig mocNapConfig = ConfigManager.getInstance().getConfig(MocNapConfig.class);
        
        // Clear existing configs
        allMilestoneConfigs.clear();
        milestoneMap.clear();
        completedMilestones.clear();
        
        // Load all milestone configs
        allMilestoneConfigs.putAll(mocNapConfig.mocnap);
        
        // Initialize milestone map with highest amount milestone for each type
        for (Map.Entry<Integer, MilestoneConfig> entry : allMilestoneConfigs.entrySet()) {
            MilestoneConfig config = entry.getValue();
            MilestoneType type = config.getType();
            
            // Only keep the highest amount milestone for each type
            milestoneMap.compute(type, (key, existingConfig) -> {
                if (existingConfig == null || config.getAmount() > existingConfig.getAmount()) {
                    return config;
                }
                return existingConfig;
            });
        }
    }

    public void computeServerMilestone(ServerMilestoneEvent event) {
        long serverTotal = SPPlugin.getInstance().getCacheDataService().getServerTotalValue().get();
        checkAndTriggerMilestones(null, serverTotal, MilestoneType.ALL);
    }

    public void computePlayerMilestone(PlayerMilestoneEvent event) {
        UUID playerUUID = event.getUuid();
        AtomicLong playerTotal = SPPlugin.getInstance().getCacheDataService().getPlayerTotalValue().get(playerUUID);
        AtomicLong playerDaily = SPPlugin.getInstance().getCacheDataService().getPlayerDailyTotalValue().get(playerUUID);
        AtomicLong playerWeekly = SPPlugin.getInstance().getCacheDataService().getPlayerWeeklyTotalValue().get(playerUUID);
        AtomicLong playerMonthly = SPPlugin.getInstance().getCacheDataService().getPlayerMonthlyTotalValue().get(playerUUID);
        AtomicLong playerYearly = SPPlugin.getInstance().getCacheDataService().getPlayerYearlyTotalValue().get(playerUUID);

        if (playerTotal != null) checkAndTriggerMilestones(playerUUID, playerTotal.get(), MilestoneType.ALL);
        if (playerDaily != null) checkAndTriggerMilestones(playerUUID, playerDaily.get(), MilestoneType.DAILY);
        if (playerWeekly != null) checkAndTriggerMilestones(playerUUID, playerWeekly.get(), MilestoneType.WEEKLY);
        if (playerMonthly != null) checkAndTriggerMilestones(playerUUID, playerMonthly.get(), MilestoneType.MONTHLY);
        if (playerYearly != null) checkAndTriggerMilestones(playerUUID, playerYearly.get(), MilestoneType.YEARLY);
    }

    private void checkAndTriggerMilestones(UUID playerUUID, long currentAmount, MilestoneType type) {
        MilestoneConfig milestoneConfig = milestoneMap.get(type);
        if (milestoneConfig == null || completedMilestones.contains(milestoneConfig)) {
            return;
        }

        // Check if milestone is achieved
        if (currentAmount >= milestoneConfig.getAmount()) {
            // Mark milestone as completed
            completedMilestones.add(milestoneConfig);
            
            // Execute milestone commands
            executeMilestoneCommands(milestoneConfig, playerUUID);
            
            // Update bossbar if configured
            updateBossBar(milestoneConfig, playerUUID);
            
            // Remove from active milestones
            milestoneMap.remove(type);
            
            // Find next milestone of this type if exists
            findNextMilestone(type);
        } else {
            // Update progress in bossbar
            updateBossBarProgress(milestoneConfig, playerUUID, currentAmount);
        }
    }

    private void findNextMilestone(MilestoneType type) {
        // Find the next highest milestone of this type that hasn't been completed
        allMilestoneConfigs.entrySet().stream()
            .filter(entry -> entry.getValue().getType() == type)
            .filter(entry -> !completedMilestones.contains(entry.getValue()))
            .max(Map.Entry.comparingByKey())
            .ifPresent(entry -> milestoneMap.put(type, entry.getValue()));
    }

    private void executeMilestoneCommands(MilestoneConfig config, UUID playerUUID) {
        if (config.getCommands() == null || config.getCommands().isEmpty()) {
            return;
        }

        SPPlugin plugin = SPPlugin.getInstance();
        plugin.getFoliaLib().getScheduler().runAsync(task -> {
            for (String command : config.getCommands()) {
                String processedCommand = command;
                if (playerUUID != null) {
                    processedCommand = command.replace("%player_name%", 
                        plugin.getServer().getOfflinePlayer(playerUUID).getName());
                }
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), processedCommand);
            }
        });
    }

    private void updateBossBar(MilestoneConfig config, UUID playerUUID) {
        if (config.getBossbar() == null || !config.getBossbar().isEnabled()) {
            return;
        }

        SPPlugin plugin = SPPlugin.getInstance();
        BossBar bossBar = BossBar.bossBar(
            net.kyori.adventure.text.Component.text(config.getBossbar().getTitle()),
            1.0f, // Initial progress
            config.getBossbar().getColor(),
            BossBar.Overlay.PROGRESS // Default overlay
        );

        if (playerUUID == null) {
            // Server milestone
            if (serverbossBar != null) {
                plugin.getServer().getOnlinePlayers().forEach(player -> serverbossBar.removeViewer(player));
            }
            serverbossBar = bossBar;
            plugin.getServer().getOnlinePlayers().forEach(player -> bossBar.addViewer(player));
        } else {
            // Player milestone
            BossBar oldBossBar = playerBossBar.get(playerUUID);
            if (oldBossBar != null) {
                Player player = plugin.getServer().getPlayer(playerUUID);
                if (player != null) {
                    oldBossBar.removeViewer(player);
                }
            }
            playerBossBar.put(playerUUID, bossBar);
            Player player = plugin.getServer().getPlayer(playerUUID);
            if (player != null) {
                bossBar.addViewer(player);
            }
        }
    }

    private void updateBossBarProgress(MilestoneConfig config, UUID playerUUID, long currentAmount) {
        if (config.getBossbar() == null || !config.getBossbar().isEnabled()) {
            return;
        }

        float progress = Math.min(1.0f, (float) currentAmount / config.getAmount());
        BossBar bossBar = playerUUID == null ? serverbossBar : playerBossBar.get(playerUUID);
        
        if (bossBar != null) {
            bossBar.progress(progress);
        }
    }

    public void invalidateMilestone(MilestoneType type) {
        MilestoneConfig config = milestoneMap.get(type);
        if (config != null) {
            completedMilestones.add(config);
            milestoneMap.remove(type);
            findNextMilestone(type);
        }
    }
}
