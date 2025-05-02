package org.simpmc.simppay.listener;

import io.papermc.paper.util.Tick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MainConfig;
import org.simpmc.simppay.config.types.MessageConfig;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.event.PaymentFailedEvent;
import org.simpmc.simppay.event.PaymentQueueSuccessEvent;
import org.simpmc.simppay.event.PaymentSuccessEvent;
import org.simpmc.simppay.handler.banking.redis.RedisHandler;
import org.simpmc.simppay.util.MessageUtil;
import org.simpmc.simppay.util.SoundUtil;

import java.time.Duration;

public class SimpPayListener implements Listener {
    public SimpPayListener(SPPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

    @EventHandler
    public void paymentQueue(PaymentQueueSuccessEvent event) {
        SPPlugin plugin = SPPlugin.getInstance();

        if (event.getPaymentType() == PaymentType.CARD) {
            addTaskChecking(event);
        }
        if (event.getPaymentType() == PaymentType.BANKING) {
            if (plugin.getPaymentService().getHandlerRegistry().getCardHandler() instanceof RedisHandler) {
                return; // Skip timer task for redis as it is event based later on
                // Whatever logic is handling should call PaymentSuccessEvent or PaymentFailedEvent on their own
                // TODO: added later on in the future
            }
            addTaskChecking(event);
        }
    }

    @EventHandler
    public void paymentSuccess(PaymentSuccessEvent event) {
        // handle success
        SPPlugin plugin = SPPlugin.getInstance();
        plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
        if (event.getPaymentType() == PaymentType.CARD) {
            // TODO
            // run card commands
            Player player = Bukkit.getPlayer(event.getPlayerUUID());

            player.sendMessage("buddy, your card is success");
        }
        if (event.getPaymentType() == PaymentType.BANKING) {
            // do banking points addition
        }
    }


    private void addTaskChecking(PaymentQueueSuccessEvent event) {
        SPPlugin plugin = SPPlugin.getInstance();
        long interval = ((MainConfig) ConfigManager.configs.get(MainConfig.class)).intervalAPICall;
        plugin.getFoliaLib().getScheduler().runTimerAsync(task -> {

            // check card status
            PaymentStatus status = null;
            if (event.getPaymentType() == PaymentType.CARD) {
                status = plugin.getPaymentService().getHandlerRegistry().getCardHandler().getTransactionStatus(event.getPaymentDetail());
            }
            if (event.getPaymentType() == PaymentType.BANKING) {
                // check banking status
                status = plugin.getPaymentService().getHandlerRegistry().getBankHandler().getTransactionStatus(event.getPaymentDetail());
            }

            switch (status) {
                case SUCCESS -> {
                    // handle success
                    // TODO: get actual amount then set into trueamount for card, the true amount should be given in the returned json
                    callEventSync(new PaymentSuccessEvent(event.getPayment()));
                    plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
                    task.cancel();
                }
                case FAILED -> {
                    // handle failed
                    callEventSync(new PaymentFailedEvent(event.getPayment()));
                    plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
                    task.cancel();
                }
                case PENDING -> {
                    // do nothing, wait for next check
                }
                case WRONG_PRICE -> {
                    // handle invalid
                    callEventSync(new PaymentSuccessEvent(event.getPayment(), true));
                    plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
                    task.cancel();
                }
                case null -> {
                    callEventSync(new PaymentFailedEvent(event.getPayment()));
                    plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
                    MessageUtil.debug("[Payment-Poller] Payment status is null");
                    task.cancel();
                }
                case INVALID -> {
                    callEventSync(new PaymentFailedEvent(event.getPayment()));
                    plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
                    task.cancel();
                }
                case EXIST -> {
                    callEventSync(new PaymentFailedEvent(event.getPayment()));
                    MessageUtil.debug("[Payment-Poller] Payment alrady exist on payment api");
                    plugin.getPaymentService().getPollingPayments().remove(event.getPayment());
                    task.cancel();
                }
            }
        }, 1L, Tick.tick().fromDuration(Duration.ofSeconds(interval)));

        plugin.getPaymentService().getPollingPayments().add(event.getPayment());


    }

    private void callEventSync(Event event) {
        SPPlugin plugin = SPPlugin.getInstance();
        plugin.getFoliaLib().getScheduler().runNextTick(wrappedTask -> Bukkit.getPluginManager().callEvent(event));
    }


}
