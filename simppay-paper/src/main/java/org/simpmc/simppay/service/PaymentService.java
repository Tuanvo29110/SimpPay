package org.simpmc.simppay.service;

import com.google.common.collect.ConcurrentHashMultiset;
import lombok.Getter;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.handler.HandlerRegistry;
import org.simpmc.simppay.model.Payment;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PaymentService {


    private ConcurrentHashMultiset<Payment> pollingPayments = ConcurrentHashMultiset.create();
    private HandlerRegistry handlerRegistry;
    private HashMap<UUID, Payment> payments = new HashMap<>();

    public PaymentService() {
        handlerRegistry = new HandlerRegistry();
    }

    public PaymentStatus sendCard(Payment payment) {
        return handlerRegistry.getCardHandler().processPayment(payment);
    }

    public void sendBank(Payment payment) {
        handlerRegistry.getBankHandler().processPayment(payment);
    }


    // use for storing data and pulling data out of the db later on


}
