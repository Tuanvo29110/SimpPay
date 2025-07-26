package org.simpmc.simppay.handler.data;

import org.simpmc.simppay.handler.ICoins;
import org.simpmc.simppay.handler.coins.CoinsEngineHandler;
import org.simpmc.simppay.handler.coins.PlayerPointsHandler;

public enum CoinsAPI {
    PLAYERPOINTS(PlayerPointsHandler.class),
    COINSENGINE(CoinsEngineHandler.class);

    public final Class<?> handlerClass;

    CoinsAPI(Class<? extends ICoins> handlerClass) {
        this.handlerClass = handlerClass;
    }
}
