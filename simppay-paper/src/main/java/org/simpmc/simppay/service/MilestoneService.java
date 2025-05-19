package org.simpmc.simppay.service;

import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MocNapConfig;
import org.simpmc.simppay.config.types.MocNapServerConfig;
import org.simpmc.simppay.config.types.data.BossBarConfig;
import org.simpmc.simppay.config.types.data.MilestoneConfig;
import org.simpmc.simppay.data.milestone.MilestoneType;
import org.simpmc.simppay.database.entities.SPPlayer;
import org.simpmc.simppay.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MilestoneService {
    //     NOTE: BossBar are static, changing bossbar reflect the changes to player who are added to it
//     Design a central bossbar of the plugin
//     Use one for player
//     one for entire server
//     config example
//     ALL:
//     - amount: 100
//       commands:
//       - "/tell %player_name% Cảm ơn đã ủng hộ server hehe"
//       - "/tell %player_name% Cảm ơn đã ủng hộ server hehe"
//     DAILY:
//     - amount: 100
//       commands:
//       - "/tell %player_name% Cảm ơn đã ủng hộ server hehe"
    public ConcurrentHashMap<UUID, List<ObjectObjectMutablePair<MilestoneType, BossBar>>> playerBossBars = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, List<MilestoneConfig>> playerCurrentMilestones = new ConcurrentHashMap<>();
    public List<MilestoneConfig> serverCurrentMilestones = new ArrayList<>();
    public List<ObjectObjectMutablePair<MilestoneType, BossBar>> serverBossbars = new ArrayList<>(); // contains all valid loaded milestones

    public MilestoneService() {
        loadAllMilestones();
    }

    public void loadAllMilestones() {
        playerCurrentMilestones.clear();
        playerBossBars.clear();
        serverCurrentMilestones.clear();
        serverBossbars.clear();
        loadServerMilestone();
        for (Player p : Bukkit.getOnlinePlayers()) { // thread-safe for folia
            loadPlayerMilestone(p.getUniqueId());
        }
    }

    // all milestones should be reloaded upon a milestone complete event
    private void loadServerMilestone() {
        SPPlugin.getInstance().getFoliaLib().getScheduler().runAsync(task -> {
            long entireServerAmount = SPPlugin.getInstance().getDatabaseService().getPaymentLogService().getEntireServerAmount();
            MocNapServerConfig mocNapServerConfig = ConfigManager.getInstance().getConfig(MocNapServerConfig.class);

            for (Map.Entry<MilestoneType, List<MilestoneConfig>> entry : mocNapServerConfig.mocnap.entrySet()) {
                MilestoneType type = entry.getKey();
                if (type == null) {
                    continue;
                }
                MessageUtil.debug("Loading MocNap Server " + type.name());
                for (MilestoneConfig config : entry.getValue()) {
                    if (config.amount <= entireServerAmount) {
                        continue;
                    }
                    if (config.type != type) {
                        config.setType(type); // auto correct
                    }
                    MessageUtil.debug("Loading MocNap Server " + type.name() + " " + config.amount);
                    BossBarConfig bossBarConfig = config.bossbar;
                    BossBar bossBar = BossBar.bossBar(
                            MessageUtil.getComponentParsed(bossBarConfig.getTitle(), null), // bossbar title will be loaded after
                            0f,
                            config.bossbar.color,
                            config.bossbar.style
                    );
                    serverBossbars.add(new ObjectObjectMutablePair<>(type, bossBar));
                }
            }
        });
    }

    public void loadPlayerMilestone(UUID uuid) {
        SPPlugin.getInstance().getFoliaLib().getScheduler().runAsync(task -> {
            SPPlayer player = SPPlugin.getInstance().getDatabaseService().getPlayerService().findByUuid(uuid);
            double playerChargedAmount = SPPlugin.getInstance().getDatabaseService().getPaymentLogService().getPlayerTotalAmount(player);

            MocNapConfig mocNapConfig = ConfigManager.getInstance().getConfig(MocNapConfig.class);
            MessageUtil.debug("Loading MocNap For Player " + player.getName());
            for (Map.Entry<MilestoneType, List<MilestoneConfig>> entry : mocNapConfig.mocnap.entrySet()) {
                MilestoneType type = entry.getKey();
                if (type == null) {
                    continue;
                }
                MessageUtil.debug("Loading MocNap Entry For Player " + type.name());

                for (MilestoneConfig config : entry.getValue()) {
                    if (config.amount <= playerChargedAmount) {
                        continue;
                    }
                    if (config.type != type) {
                        config.setType(type); // auto correct
                    }
                    MessageUtil.debug("Loading MocNap Entry For Player " + type.name() + " " + config.amount);
                    BossBarConfig bossBarConfig = config.bossbar;
                    BossBar bossBar = BossBar.bossBar(
                            MessageUtil.getComponentParsed(bossBarConfig.getTitle(), null), // bossbar title will be loaded after
                            0f,
                            config.bossbar.color,
                            config.bossbar.style
                    );
                    MessageUtil.debug("Loaded MocNap Entry For Player " + type.name() + " " + config.amount);
                    playerBossBars.get(uuid).add(new ObjectObjectMutablePair<>(type, bossBar));
                }
                playerCurrentMilestones.put(uuid, entry.getValue());
            }
        });


    }

}
