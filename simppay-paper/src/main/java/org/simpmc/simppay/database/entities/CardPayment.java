package org.simpmc.simppay.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.data.card.CardType;
import org.simpmc.simppay.model.detail.CardDetail;

import java.util.UUID;

@DatabaseTable(tableName = "card_payments")
public class CardPayment {

    @DatabaseField(id = true, dataType = DataType.UUID)
    private UUID paymentID;

    @DatabaseField(dataType = DataType.UUID, columnName = "player_uuid", canBeNull = false)
    private UUID playerUUID;

    /**
     * Always CARD for this table
     */
    @DatabaseField(columnName = "payment_type", canBeNull = false)
    private PaymentType paymentType = PaymentType.CARD;

    // Card‐specific fields:
    @DatabaseField(columnName = "pin", canBeNull = false)
    private String pin;

    @DatabaseField(columnName = "serial", canBeNull = false)
    private String serial;

    @DatabaseField(columnName = "price_value", canBeNull = false)
    private double priceValue;

    @DatabaseField(columnName = "card_type", canBeNull = false)
    private CardType cardType;

    @DatabaseField(columnName = "ref_id", canBeNull = false)
    private String refID;

    @DatabaseField(columnName = "true_amount", canBeNull = true)
    private double trueAmount;

    public CardPayment() {
    }

    public CardPayment(Payment payment) {
        this.paymentID = payment.getPaymentID();
        this.playerUUID = payment.getPlayerUUID();
        this.pin = ((CardDetail) payment.getDetail()).getPin();
        this.serial = ((CardDetail) payment.getDetail()).getSerial();
        this.priceValue = ((CardDetail) payment.getDetail()).getPrice().getValue();
        this.cardType = ((CardDetail) payment.getDetail()).getType();
        this.refID = payment.getDetail().getRefID();
        this.trueAmount = ((CardDetail) payment.getDetail()).getTrueAmount();
    }

    // … getters & setters …
}
