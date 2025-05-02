package org.simpmc.simppay.model.detail;

public interface PaymentDetail {
    double getAmount();

    String getRefID();

    void setRefID(String refID);

    double getTrueAmount();
}
