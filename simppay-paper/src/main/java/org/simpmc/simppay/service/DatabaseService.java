package org.simpmc.simppay.service;

import lombok.Getter;
import org.simpmc.simppay.database.Database;
import org.simpmc.simppay.service.database.PaymentLogService;
import org.simpmc.simppay.service.database.PlayerDataService;
import org.simpmc.simppay.service.database.PlayerService;

@Getter
public class DatabaseService {
    private final PlayerService playerService;
    private final PaymentLogService paymentLogService;
    private final PlayerDataService playerDataService;

    public DatabaseService(Database database) {
        playerService = new PlayerService(database.getPlayerDao());
        paymentLogService = new PaymentLogService(database);
        playerDataService = new PlayerDataService(database.getPlayerDataDao());
    }


}
