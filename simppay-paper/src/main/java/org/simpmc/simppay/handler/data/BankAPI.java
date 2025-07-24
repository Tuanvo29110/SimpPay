package org.simpmc.simppay.handler.data;

import org.simpmc.simppay.handler.BankHandler;
import org.simpmc.simppay.handler.PaymentHandler;
import org.simpmc.simppay.handler.banking.payos.PayosHandler;

public enum BankAPI {
    PAYOS(PayosHandler.class);

    public final Class<? extends BankHandler> handlerClass;

    BankAPI(Class<? extends BankHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }
}
