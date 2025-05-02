package org.simpmc.simppay.model;

import lombok.Data;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.model.detail.CardDetail;
import org.simpmc.simppay.model.detail.PaymentDetail;

import java.util.UUID;

@Data
public class Payment {

    private final UUID paymentID; // Internal ID of the plugin
    private final UUID playerUUID;
    private final PaymentType paymentType;

    private final PaymentDetail detail;
    private PaymentStatus status;

    public Payment(UUID paymentID, UUID playerUUID, PaymentDetail detail) {
        this.paymentID = paymentID;
        this.playerUUID = playerUUID;
        this.detail = detail;
        if (detail instanceof CardDetail) {
            this.paymentType = PaymentType.CARD;
        } else {
            this.paymentType = PaymentType.BANKING;
        }
        this.status = null;
    }

}
