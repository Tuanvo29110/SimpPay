package org.simpmc.simppay.handler;

import lombok.Getter;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.BankingConfig;
import org.simpmc.simppay.config.types.CardConfig;
import org.simpmc.simppay.config.types.CoinsConfig;
import org.simpmc.simppay.util.MessageUtil;

import java.lang.reflect.InvocationTargetException;

@Getter
public class HandlerRegistry {

    private PaymentHandler cardHandler;
    private PaymentHandler bankHandler;

    private CoinsHandler coinsHandler;

    public HandlerRegistry() {
        try {
            init();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Only call this once
    private void init() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        CardConfig cardConfig = ConfigManager.getInstance().getConfig(CardConfig.class);
        BankingConfig bankingConfig = ConfigManager.getInstance().getConfig(BankingConfig.class);
        CoinsConfig coinsConfig = ConfigManager.getInstance().getConfig(CoinsConfig.class);

        cardHandler = (PaymentHandler) cardConfig.cardApi.handlerClass.getDeclaredConstructor().newInstance();
        bankHandler = (PaymentHandler) bankingConfig.bankApi.handlerClass.getDeclaredConstructor().newInstance();
        coinsHandler = (CoinsHandler) coinsConfig.pointsProvider.handlerClass.getDeclaredConstructor().newInstance();
        MessageUtil.info("Registered handlers: ");
        MessageUtil.info("Card Handler: " + cardHandler.getClass().getSimpleName());
        MessageUtil.info("Bank Handler: " + bankHandler.getClass().getSimpleName());
        MessageUtil.info("Coins Handler: " + coinsHandler.getClass().getSimpleName());
    }

    public void reload() {
        try {
            init();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
