package org.simpmc.simppay.listener.internal.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MessageConfig;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.event.PaymentQueueSuccessEvent;
import org.simpmc.simppay.event.PaymentSuccessEvent;
import org.simpmc.simppay.util.MessageUtil;
import org.simpmc.simppay.util.SoundUtil;


// Handle all success event targeted to players

public class SucessHandlingListener implements Listener {
    public SucessHandlingListener(SPPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void removeCaching(PaymentSuccessEvent event) {
        SPPlugin plugin = SPPlugin.getInstance();

        plugin.getPaymentService().getPayments().remove(event.getPayment().getPaymentID());

        if (event.getPaymentType() == PaymentType.BANKING) {
            SPPlugin.getInstance().getPaymentService().clearPlayerBankCache(event.getPlayerUUID());
            Player player = Bukkit.getPlayer(event.getPlayerUUID());
            if (player != null) {
                player.updateInventory(); // update the fake qr map to normal item
            }

        }
    }

    @EventHandler
    public void giveCoins(PaymentSuccessEvent event) {
        // handle success
        SPPlugin plugin = SPPlugin.getInstance();
        Player player = Bukkit.getPlayer(event.getPlayerUUID());
        MessageConfig config = (MessageConfig) ConfigManager.configs.get(MessageConfig.class);

        if (event.getPaymentType() == PaymentType.CARD) {
            // TODO
            // run card commands

            MessageUtil.sendMessage(player, config.successPayment.replace("<amount>", String.valueOf(event.getAmount())));
        }
        if (event.getPaymentType() == PaymentType.BANKING) {

            MessageUtil.sendMessage(player, config.successPayment.replace("<amount>", String.valueOf(event.getAmount())));
        }
    }

    @EventHandler
    public void notifyPlayer(PaymentQueueSuccessEvent event) {

        MessageConfig config = (MessageConfig) ConfigManager.configs.get(MessageConfig.class);
        // notify player
        if (event.getPaymentType() == PaymentType.CARD) {
            MessageUtil.sendMessage(event.getPlayerUUID(), config.successQueueCard);
            SoundUtil.sendSound(event.getPlayerUUID(), config.soundEffect.get(PaymentStatus.PENDING).toSound());
        }
        if (event.getPaymentType() == PaymentType.BANKING) {
            // called when player receive the qr code and the task for checking api is running
            MessageUtil.sendMessage(event.getPlayerUUID(), config.successQueueBanking);
            SoundUtil.sendSound(event.getPlayerUUID(), config.soundEffect.get(PaymentStatus.PENDING).toSound());
        }

    }
}
