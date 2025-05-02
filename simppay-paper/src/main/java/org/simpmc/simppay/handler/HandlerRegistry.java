package org.simpmc.simppay.handler;

import lombok.Getter;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.BankingConfig;
import org.simpmc.simppay.config.types.CardConfig;

import java.lang.reflect.InvocationTargetException;

@Getter
public class HandlerRegistry {

    private PaymentHandler cardHandler;
    private PaymentHandler bankHandler;

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

        CardConfig cardConfig = (CardConfig) ConfigManager.configs.get(CardConfig.class);
        BankingConfig bankingConfig = (BankingConfig) ConfigManager.configs.get(BankingConfig.class);

        cardHandler = (PaymentHandler) cardConfig.cardAPI.handlerClass.getDeclaredConstructor().newInstance();
        bankHandler = (PaymentHandler) bankingConfig.bankAPI.handlerClass.getDeclaredConstructor().newInstance();

    }

}
