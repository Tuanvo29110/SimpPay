package org.simpmc.simppay.handler;

import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.model.PaymentResult;
import org.simpmc.simppay.model.detail.PaymentDetail;

import java.util.concurrent.ExecutionException;

public interface PaymentHandler {

    PaymentStatus processPayment(Payment payment) throws ExecutionException, InterruptedException; // should only return pending or exist

    PaymentResult getTransactionResult(PaymentDetail detail) throws ExecutionException, InterruptedException;

    PaymentStatus cancel(Payment payment);
}
