package org.simpmc.simppay.model.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BankingDetail implements PaymentDetail {

    private long amount;
    private String refID;
    private String description;
    private String QRCode;

    @Override
    public long getAmount() {
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
