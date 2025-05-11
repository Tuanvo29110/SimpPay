package org.simpmc.simppay.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.simpmc.simppay.SPPlugin;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener(SPPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        SPPlugin plugin = SPPlugin.getInstance();
        plugin.getFoliaLib().getScheduler().runAsync(task2 -> plugin.getPlayerService().createPlayer(e.getPlayer()));
        plugin.getFoliaLib().getScheduler().runAsync(task2 -> plugin.getCacheDataService().addPlayerToQueue(e.getPlayer().getUniqueId()));

    }
}
