package org.simpmc.simppay.handler.data;

import org.simpmc.simppay.handler.PaymentHandler;
import org.simpmc.simppay.handler.card.TSTHandler;

public enum CardAPI {
    THESIEUTOC(TSTHandler.class);

    public final Class<?> handlerClass;

    CardAPI(Class<? extends PaymentHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }

}
