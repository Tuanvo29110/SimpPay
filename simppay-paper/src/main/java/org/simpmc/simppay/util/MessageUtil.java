package org.simpmc.simppay.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MainConfig;
import org.simpmc.simppay.config.types.MessageConfig;

import java.util.UUID;

public class MessageUtil {
    public static void sendMessage(Player player, Component message) {
        SPPlugin.getInstance().getFoliaLib().getScheduler().runAtEntity(player, task -> {

            MessageConfig messageConfig = (MessageConfig) ConfigManager.configs.get(MessageConfig.class);

            Component prefix = messageConfig.prefix;
            player.sendMessage(prefix.append(message));
        });
    }

    public static void sendMessage(UUID playerUuid, Component message) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) {
            return;
        }
        SPPlugin.getInstance().getFoliaLib().getScheduler().runAtEntity(player, task -> {
            MessageConfig messageConfig = (MessageConfig) ConfigManager.configs.get(MessageConfig.class);
            Component prefix = messageConfig.prefix;
            player.sendMessage(prefix.append(message));
        });
    }

    public static void debug(String message) {

        MainConfig mainConfig = (MainConfig) ConfigManager.configs.get(MainConfig.class);
        if (mainConfig.debug) {
            SPPlugin.getInstance().getLogger().info(message);
        }
    }
}
