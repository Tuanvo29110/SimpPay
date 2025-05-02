package org.simpmc.simppay.handler.card;

import com.google.gson.JsonObject;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.card.ThesieutocConfig;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.data.card.CardType;
import org.simpmc.simppay.data.card.thesieutoc.TSTCardAdapter;
import org.simpmc.simppay.event.PaymentQueueSuccessEvent;
import org.simpmc.simppay.exception.CardProcessException;
import org.simpmc.simppay.handler.CardAdapter;
import org.simpmc.simppay.handler.PaymentHandler;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.model.detail.CardDetail;
import org.simpmc.simppay.model.detail.PaymentDetail;
import org.simpmc.simppay.util.MessageUtil;
import org.simpmc.simppay.utils.HashUtil;
import org.simpmc.simppay.utils.HttpUtils;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@NoArgsConstructor
public class TSTHandler implements PaymentHandler, CardAdapter {

    // TODO: Improve to GSON ?
    @Override
    public PaymentStatus processPayment(Payment payment) {
        CardDetail detail = (CardDetail) payment.getDetail();

        JsonObject request;
        try {
            request = requestTransaction(detail).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CardProcessException(e.getMessage());
        }
        if (request == null) {
            MessageUtil.debug("[Thesieutoc-ProcessPayment] Request is null");
            return PaymentStatus.FAILED;
        }
        if (request.get("status").getAsInt() == 2) {
            MessageUtil.debug("[Thesieutoc-ProcessPayment]" + request);
            return PaymentStatus.FAILED;
        }
        if (request.get("status").getAsInt() == 00) {
            MessageUtil.debug("[Thesieutoc-ProcessPayment]" + request);
            String refID = request.get("transaction_id").getAsString();
            payment.getDetail().setRefID(refID);
            Bukkit.getPluginManager().callEvent(new PaymentQueueSuccessEvent(payment));
            return PaymentStatus.PENDING;
        }
        MessageUtil.debug("[Thesieutoc-ProcessPayment]" + request);
        // default to failed if other status codes
        return PaymentStatus.FAILED;
    }

    private CompletableFuture<JsonObject> requestTransaction(CardDetail card) {
        return CompletableFuture.supplyAsync(() -> {
            ThesieutocConfig config = (ThesieutocConfig) ConfigManager.configs.get(ThesieutocConfig.class);

            if (config.apiKey == null || config.secretKey == null) {
                throw new CardProcessException("API key or secret key is not set");
            }
            String base = "http://vnpt.thesieutoc.net/API/transaction?APIkey={0}&APIsecret={1}&mathe={2}&seri={3}&type={4}&menhgia={5}";
            String rnd = HashUtil.randomMD5();
            String url = MessageFormat.format(base,
                    config.apiKey,
                    config.secretKey,
                    card.pin,
                    card.serial,
                    adaptCardType(card.type),
                    String.valueOf(TSTCardAdapter.getCardPriceID(card.price))
            ).replace("\"", "");

            JsonObject json = HttpUtils.getJsonResponse(url);
            if (json != null) {
                json.addProperty("randomMD5", rnd);
            }
            return json;
        });
    }

    public PaymentStatus getTransactionStatus(PaymentDetail card) {
        ThesieutocConfig config = (ThesieutocConfig) ConfigManager.configs.get(ThesieutocConfig.class);
        String base = "http://vnpt.thesieutoc.net/API/get_status_card.php?APIkey={0}&APIsecret={1}&transaction_id={2}";
        String url = MessageFormat.format(base,
                config.apiKey,
                config.secretKey,
                card.getRefID()
        );

        // todo: scheduler
        JsonObject json = HttpUtils.getJsonResponse(url);
        if (json != null) {
            json.addProperty("randomMD5", card.getRefID());
        }
        if (json == null) {
            MessageUtil.debug("[Thesieutoc-GetTransactionStatus] Request is null");
            return PaymentStatus.FAILED;
        }
        MessageUtil.debug("[Thesieutoc-ProcessPayment]" + json);
        int status = json.get("status").getAsInt();
        PaymentStatus paymentStatus = TSTCardAdapter.getCardStatus(status);
        return paymentStatus;
    }

    @Override
    public String adaptCardType(CardType cardType) {
        switch (cardType) {
            case VIETTEL -> {
                return "Viettel";
            }
            case VINAPHONE -> {
                return "Vinaphone";
            }
            case VIETNAMOBILE -> {
                return "Vietnamobile";
            }
            case MOBIFONE -> {
                return "Mobifone";
            }
            case GATE -> {
                return "Gate";
            }
            case ZING -> {
                return "Zing";
            }
            case GARENA -> {
                return "Garena";
            }
            case VCOIN -> {
                return "Vcoin";
            }
        }

        return null;
    }
}

