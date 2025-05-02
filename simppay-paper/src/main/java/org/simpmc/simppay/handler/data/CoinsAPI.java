package org.simpmc.simppay.handler.data;

import org.simpmc.simppay.handler.CoinsHandler;
import org.simpmc.simppay.handler.coins.PlayerPointsHandler;

public enum CoinsAPI {
    PLAYERPOINTS(PlayerPointsHandler.class),
    ;

    public final Class<?> handlerClass;

    CoinsAPI(Class<? extends CoinsHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }
}
