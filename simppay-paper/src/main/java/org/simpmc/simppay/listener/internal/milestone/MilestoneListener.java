package org.simpmc.simppay.listener.internal.milestone;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.event.PlayerMilestoneEvent;
import org.simpmc.simppay.event.ServerMilestoneEvent;

import java.util.HashMap;
import java.util.UUID;

public class MilestoneListener implements Listener {
    public MilestoneListener(SPPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();
        BossBar bossBar = SPPlugin.getInstance().getMilestoneService().getPlayerBossBar().get(uuid);
        BossBar serverBossBar = SPPlugin.getInstance().getMilestoneService().getServerbossBar();
        if (bossBar != null) {
            bossBar.addViewer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMilestone(PlayerMilestoneEvent event) {
        SPPlugin.getInstance().getMilestoneService().computePlayerMilestone(event);
    }

    @EventHandler
    public void onServerMilestone(ServerMilestoneEvent event) {
        SPPlugin.getInstance().getMilestoneService().computeServerMilestone(event);
    }
}
