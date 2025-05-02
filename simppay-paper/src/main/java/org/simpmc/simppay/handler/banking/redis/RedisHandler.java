package org.simpmc.simppay.handler.banking.redis;

import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.handler.PaymentHandler;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.model.detail.PaymentDetail;

public class RedisHandler implements PaymentHandler {
    @Override
    public PaymentStatus processPayment(Payment payment) {

        return null;
    }

    @Override
    public PaymentStatus getTransactionStatus(PaymentDetail detail) {
        return null;
    }
}
