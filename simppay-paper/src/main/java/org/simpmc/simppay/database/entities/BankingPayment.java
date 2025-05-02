package org.simpmc.simppay.database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.model.Payment;

import java.util.UUID;

@Data
@DatabaseTable(tableName = "banking_payments")
public class BankingPayment {

    @DatabaseField(id = true, dataType = DataType.UUID)
    private UUID paymentID;

    @DatabaseField(dataType = DataType.UUID, columnName = "player_uuid", canBeNull = false)
    private UUID playerUUID;

    /**
     * Always BANKING for this table
     */
    @DatabaseField(columnName = "payment_type", canBeNull = false)
    private PaymentType paymentType = PaymentType.BANKING;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private double amount;

    @DatabaseField(columnName = "ref_id", canBeNull = false)
    private String refID;

    // ORMLite needs a no-arg constructor
    public BankingPayment() {
    }

    public BankingPayment(UUID paymentID, UUID playerUUID, double amount, String refID) {
        this.paymentID = paymentID;
        this.playerUUID = playerUUID;
        this.amount = amount;
        this.refID = refID;
    }


    public BankingPayment(Payment payment) {
        this.paymentID = payment.getPaymentID();
        this.playerUUID = payment.getPlayerUUID();
        this.amount = payment.getDetail().getAmount();
        this.refID = payment.getDetail().getRefID();

    }
}
