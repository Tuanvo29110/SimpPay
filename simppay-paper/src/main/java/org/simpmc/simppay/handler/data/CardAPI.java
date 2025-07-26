package org.simpmc.simppay.handler.data;

import org.simpmc.simppay.handler.CardHandler;
import org.simpmc.simppay.handler.PaymentHandler;
import org.simpmc.simppay.handler.card.Card2KHandler;
import org.simpmc.simppay.handler.card.GT1SHandler;
import org.simpmc.simppay.handler.card.TSTHandler;

public enum CardAPI {
    THESIEUTOC(TSTHandler.class),
    CARD2K(Card2KHandler.class),
    GT1SCOM(GT1SHandler.class);

    public final Class<? extends CardHandler> handlerClass;

    CardAPI(Class<? extends CardHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }

}
