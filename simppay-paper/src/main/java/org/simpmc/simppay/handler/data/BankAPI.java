package org.simpmc.simppay.handler.data;

import org.simpmc.simppay.handler.PaymentHandler;
import org.simpmc.simppay.handler.banking.payos.PayosHandler;
import org.simpmc.simppay.handler.banking.redis.RedisHandler;

public enum BankAPI {
    PAYOS(PayosHandler.class),
    REDIS(RedisHandler.class);

    public final Class<?> handlerClass;

    BankAPI(Class<? extends PaymentHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }
}
