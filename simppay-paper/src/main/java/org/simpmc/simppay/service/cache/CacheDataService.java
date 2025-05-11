package org.simpmc.simppay.service.cache;

import lombok.Getter;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.database.entities.SPPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

// TODO: Rewrite this class to use a more efficient data structure, use generics stuff thingy, or libraries
@Getter
public class CacheDataService {
    private final ConcurrentHashMap<UUID, Long> playerTotalValue = new ConcurrentHashMap<>();

    private final ConcurrentLinkedQueue<UUID> playerQueue = new ConcurrentLinkedQueue<>();

    private final AtomicLong serverTotalValue = new AtomicLong(0);

    public void clearAllCache() {
        playerTotalValue.clear();
    }

    public void removePlayerCache(UUID playerUUID) {
        playerTotalValue.remove(playerUUID);
    }

    // Process queue conurrently
    public void addPlayerToQueue(UUID playerUUID) {
        playerQueue.add(playerUUID);
    }

    public void processQueue() {
        while (!playerQueue.isEmpty()) {
            SPPlugin plugin = SPPlugin.getInstance();
            UUID playerUUID = playerQueue.poll();

            SPPlayer player = plugin.getPlayerService().findByUuid(playerUUID);

            double totalValue = plugin.getPaymentLogService().getPlayerTotalAmount(player);
            if (totalValue != 0) {
                playerTotalValue.put(playerUUID, (long) totalValue);
                serverTotalValue.set(plugin.getPaymentLogService().getEntireServerAmount());
            }
        }
    }
}
