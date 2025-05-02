package org.simpmc.simppay.model.detail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankingDetail implements PaymentDetail {

    private double amount;
    private String refID;

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getRefID() {
        return refID;
    }

    @Override
    public double getTrueAmount() {
        return amount;
    }
}
