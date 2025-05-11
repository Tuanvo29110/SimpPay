package org.simpmc.simppay.listener.internal.player.database;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.event.PaymentSuccessEvent;

// fking ass class name, longgg
public class SuccessDatabaseHandlingListener implements Listener {
    public SuccessDatabaseHandlingListener(SPPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void updateDBz(PaymentSuccessEvent event) {
        SPPlugin plugin = SPPlugin.getInstance();

        plugin.getFoliaLib().getScheduler().runAsync(task -> {
            plugin.getPaymentLogService().addPayment(event.getPayment());
        });
    }

    @EventHandler
    public void updateQueue(PaymentSuccessEvent event) {
        SPPlugin plugin = SPPlugin.getInstance();

        plugin.getFoliaLib().getScheduler().runAsync(task -> {
            plugin.getCacheDataService().addPlayerToQueue(event.getPlayerUUID());
        });
    }
}
