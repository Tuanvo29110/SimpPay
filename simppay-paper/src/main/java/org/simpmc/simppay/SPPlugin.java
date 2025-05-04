package org.simpmc.simppay;

import com.github.retrooper.packetevents.PacketEvents;
import com.tcoded.folialib.FoliaLib;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpmc.simppay.api.DatabaseSettings;
import org.simpmc.simppay.commands.CommandHandler;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.DatabaseConfig;
import org.simpmc.simppay.database.Database;
import org.simpmc.simppay.listener.internal.payment.PaymentHandlingListener;
import org.simpmc.simppay.listener.internal.player.BankPromptListener;
import org.simpmc.simppay.listener.internal.player.SucessHandlingListener;
import org.simpmc.simppay.service.OrderIDService;
import org.simpmc.simppay.service.PaymentService;

import java.sql.SQLException;
import java.util.Set;

public final class SPPlugin extends JavaPlugin {

    @Getter
    private static SPPlugin instance;
    @Getter
    private ConfigManager configManager;
    @Getter
    private FoliaLib foliaLib;
    @Getter
    private Database database;
    @Getter
    private PaymentService paymentService;
    @Getter
    private CommandHandler commandHandler;

    private boolean dev = false;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        commandHandler = new CommandHandler(this);
        commandHandler.onLoad();
    }

    @Override
    public void onEnable() {
        // Reset config
        PacketEvents.getAPI().init();
        new Metrics(this, 25553);

        // Thanks CHATGPT, qua met r
        OrderIDService.init(this);
        instance = this;
        foliaLib = new FoliaLib(this);
        // Plugin startup logic
        configManager = new ConfigManager(this);
        paymentService = new PaymentService();
        registerListener();
        commandHandler.onEnable();


        try {
            DatabaseSettings databaseConf = (DatabaseSettings) ConfigManager.configs.get(DatabaseConfig.class);
            database = new Database(databaseConf);
        } catch (RuntimeException | SQLException e) {
            getLogger().warning("ChuyenXu failed to connect to database");
            this.getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PacketEvents.getAPI().terminate();
        if (database != null) {
            database.close();
        }
        commandHandler.onDisable();
        instance = null;
    }

    private void registerListener() {
        Set<Class<? extends Listener>> listeners = Set.of(
                PaymentHandlingListener.class,
                BankPromptListener.class,
                SucessHandlingListener.class
        );

        for (Class<? extends Listener> listener : listeners) {
            try {
                listener.getConstructor(SPPlugin.class).newInstance(this);
            } catch (Exception e) {
                getLogger().warning("Failed to register listener: " + listener.getSimpleName());
                e.printStackTrace();
            }
        }
    }

}
