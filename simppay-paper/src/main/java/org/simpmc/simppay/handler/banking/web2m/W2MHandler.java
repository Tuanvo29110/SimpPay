package org.simpmc.simppay.handler.banking.web2m;

import org.bukkit.Bukkit;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.banking.W2MConfig;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.data.bank.BankType;
import org.simpmc.simppay.event.PaymentBankPromptEvent;
import org.simpmc.simppay.event.PaymentQueueSuccessEvent;
import org.simpmc.simppay.handler.BankHandler;
import org.simpmc.simppay.handler.banking.data.BankingData;
import org.simpmc.simppay.handler.banking.web2m.data.W2MReponse;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.model.PaymentResult;
import org.simpmc.simppay.model.detail.PaymentDetail;
import org.simpmc.simppay.util.GsonUtil;
import org.simpmc.simppay.util.MessageUtil;

import java.util.concurrent.ExecutionException;

public class W2MHandler extends BankHandler {
    W2MConfig w2mConfig = ConfigManager.getInstance().getConfig(W2MConfig.class);

    String urlBase = "https://api.web2m.com/";

    String username = w2mConfig.login;
    String password = w2mConfig.password;
    String token = w2mConfig.token;

    BankType bank = w2mConfig.bankType;
    String accountNumber = w2mConfig.accountNumber;

    @Override
    public PaymentStatus processPayment(Payment payment) {
        if (accountNumber.equals("123123123")) { // default value
            return PaymentStatus.FAILED;
        }
        String refId = payment.getPlayerUUID().toString().replace("-", "");
        PaymentDetail detail = payment.getDetail();

        detail.setRefID(refId);

        BankingData bankData = BankingData.builder()
                .bin(bank.bin)
                .playerUUID(payment.getPlayerUUID())
                .desc(refId)
                .amount(detail.getAmount())
                .url(null)
                .accountNumber(w2mConfig.accountNumber)
                .qrString(null)
                .build();
        MessageUtil.debug("[W2M-ProcessPayment]" + bankData);
        Bukkit.getPluginManager().callEvent(new PaymentQueueSuccessEvent(payment));
        Bukkit.getPluginManager().callEvent(new PaymentBankPromptEvent(bankData));
        return PaymentStatus.PENDING;
    }

    @Override
    public PaymentResult getTransactionResult(PaymentDetail detail) {
        String url;
        if (w2mConfig.bankType.isOneParam) {
            url = urlBase + w2mConfig.bankType.web2mPath + "/" + password + "/" + username + "/" + token;
        } else {
            url = urlBase + w2mConfig.bankType.web2mPath + "/" + token;
        }
        String response;
        try {
            response = get(url).get();
            MessageUtil.debug("[W2M-GetTransactionResult] Response: " + response);
        } catch (InterruptedException | ExecutionException e) {
            MessageUtil.debug("[W2M-GetTransactionResult] Error while getting transaction result: " + e.getMessage());
            return new PaymentResult(
                    PaymentStatus.FAILED,
                    (int) detail.getAmount(),
                    ""
            );
        }
        W2MReponse w2mResponse = GsonUtil.getGson().fromJson(response, W2MReponse.class);
        if (w2mResponse == null) {
            MessageUtil.debug("[W2M-GetTransactionResult] Response is not valid");
            return new PaymentResult(
                    PaymentStatus.FAILED,
                    (int) detail.getAmount(),
                    ""
            );
        }
        if (!w2mResponse.getStatus()) {
            MessageUtil.debug("[W2M-GetTransactionResult] Invalid login or token");
            MessageUtil.debug("[W2M-GetTransactionResult] " + w2mResponse);
            return new PaymentResult(
                    PaymentStatus.FAILED,
                    (int) detail.getAmount(),
                    ""
            );
        }
        if (w2mResponse.getStatus()) {
            boolean matched = w2mResponse.getTransactions().stream().anyMatch(tx -> tx.getDescription().contains(detail.getRefID()));
            if (matched) {
                MessageUtil.debug("[W2M-GetTransactionResult] Transaction found for " + detail.getRefID());
                return new PaymentResult(
                        PaymentStatus.SUCCESS,
                        (int) detail.getAmount(),
                        ""
                );
            } else {
                // note: expire after 5 minutes by default, logic in PaymentHandlingListener
                MessageUtil.debug("[W2M-GetTransactionResult] No transaction found for " + detail.getRefID());
                return new PaymentResult(
                        PaymentStatus.PENDING,
                        (int) detail.getAmount(),
                        ""
                );
            }
        }
        return new PaymentResult(
                PaymentStatus.FAILED,
                (int) detail.getAmount(),
                ""
        );
    }

    @Override
    public PaymentStatus cancel(Payment payment) {
        return null;
    }
}
