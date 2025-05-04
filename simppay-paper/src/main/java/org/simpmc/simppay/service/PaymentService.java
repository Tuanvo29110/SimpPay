package org.simpmc.simppay.service;

import lombok.Getter;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.handler.HandlerRegistry;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.util.MessageUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PaymentService {


    private final ConcurrentHashMap<UUID, Payment> pollingPayments = new ConcurrentHashMap<>(); // payment id is key
    private final HandlerRegistry handlerRegistry;
    private final ConcurrentHashMap<UUID, Payment> payments = new ConcurrentHashMap<>(); // payment id is key
    private final ConcurrentHashMap<UUID, UUID> playerBankingSessionPayment = new ConcurrentHashMap<>(); // Store player uuid and payment id
    private final ConcurrentHashMap<UUID, byte[]> playerBankQRCode = new ConcurrentHashMap<>(); // Store player uuid and VietQR map bytew

    public PaymentService() {
        handlerRegistry = new HandlerRegistry();
    }

    public PaymentStatus sendCard(Payment payment) {
        payments.putIfAbsent(payment.getPaymentID(), payment);
        return handlerRegistry.getCardHandler().processPayment(payment);
    }

    public PaymentStatus sendBank(Payment payment) {
        payments.putIfAbsent(payment.getPaymentID(), payment);
        return handlerRegistry.getBankHandler().processPayment(payment);
    }

    public void clearPlayerBankCache(UUID playerUUID) {
        playerBankQRCode.remove(playerUUID);
        playerBankingSessionPayment.remove(playerUUID);
    }

    public void removePlayerQRSession(UUID playerUUID) {
        UUID paymentID = playerBankingSessionPayment.get(playerUUID);
        int retryCount = 0;
        boolean cancelled = false;

        while (retryCount < 5 && !cancelled) {
            PaymentStatus status = handlerRegistry.getBankHandler().cancel(payments.get(paymentID)); // call to cancel payment

            if (status == PaymentStatus.CANCELLED) {
                MessageUtil.debug("[PaymentService-Cancel] " + payments.get(paymentID));
                cancelled = true;
            } else {
                MessageUtil.debug("[PaymentService-Cancel] " + payments.get(paymentID) + " failed to cancel, retrying...");
                retryCount++;
            }
        }

        if (!cancelled) {
            SPPlugin.getInstance().getLogger().info("[PaymentService-Cancel] Max retries reached for " + payments.get(paymentID));
            return;
        }

        payments.remove(paymentID); // remove payment from existing payment on the server
        pollingPayments.remove(paymentID); // remove payment from polling payments
        playerBankingSessionPayment.remove(playerUUID);
        playerBankQRCode.remove(playerUUID);
    }


    // use for storing data and pulling data out of the db later on


}
