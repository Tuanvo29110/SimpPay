package org.simpmc.simppay.model.detail;

public interface PaymentDetail {
    long getAmount();

    String getRefID();

    void setRefID(String refID);

    String getDescription();

    String getQRCode();

    double getTrueAmount();
}
