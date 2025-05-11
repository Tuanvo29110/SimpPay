package org.simpmc.simppay.hook.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.service.cache.CacheDataService;

import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion implements Taskable {
    private final SPPlugin plugin;

    public PlaceholderAPIHook(SPPlugin plugin) {
        this.plugin = plugin;
        register();
    }

    @Override
    public void start() {
        // Process queue for caching
        plugin.getFoliaLib().getScheduler().runTimerAsync(task -> {
            plugin.getCacheDataService().processQueue();
        }, 20, 20);

    }

    @Override
    public void stop() {
        // Clear cache
        plugin.getCacheDataService().clearAllCache();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simppay";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Typical";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; //
    }


    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) {
            return null;
        }

        CacheDataService cacheDataService = plugin.getCacheDataService();
        UUID uuid = player.getUniqueId();

        if (!cacheDataService.getPlayerTotalValue().containsKey(uuid)) {
            cacheDataService.addPlayerToQueue(uuid);
            return "Đang load...";
        }

        String[] args = identifier.split("_");
        // %simppay_total%
        if (args[0].equalsIgnoreCase("total") && args.length == 1) {
            return cacheDataService.getPlayerTotalValue().get(uuid).toString();
        }
        // get server_total
        // %simppay_server_total%
        if (args[0].equalsIgnoreCase("server") && args[1].equalsIgnoreCase("total") && args.length == 2) {
            return cacheDataService.getServerTotalValue().toString(); // cached
        }
        return null;
    }

}
