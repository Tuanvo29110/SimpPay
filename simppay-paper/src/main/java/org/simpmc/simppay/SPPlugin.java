package org.simpmc.simppay;

import com.github.retrooper.packetevents.PacketEvents;
import com.tcoded.folialib.FoliaLib;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import me.devnatan.inventoryframework.AnvilInputFeature;
import me.devnatan.inventoryframework.ViewFrame;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpmc.simppay.api.DatabaseSettings;
import org.simpmc.simppay.commands.CommandHandler;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.DatabaseConfig;
import org.simpmc.simppay.database.Database;
import org.simpmc.simppay.hook.HookManager;
import org.simpmc.simppay.listener.PlayerJoinListener;
import org.simpmc.simppay.listener.internal.payment.PaymentHandlingListener;
import org.simpmc.simppay.listener.internal.player.BankPromptListener;
import org.simpmc.simppay.listener.internal.player.SuccessHandlingListener;
import org.simpmc.simppay.listener.internal.player.database.SuccessDatabaseHandlingListener;
import org.simpmc.simppay.menu.PaymentHistoryView;
import org.simpmc.simppay.menu.card.CardListView;
import org.simpmc.simppay.menu.card.CardPriceView;
import org.simpmc.simppay.menu.card.anvil.CardPINView;
import org.simpmc.simppay.menu.card.anvil.CardSerialView;
import org.simpmc.simppay.service.OrderIDService;
import org.simpmc.simppay.service.PaymentService;
import org.simpmc.simppay.service.cache.CacheDataService;
import org.simpmc.simppay.service.database.PaymentLogService;
import org.simpmc.simppay.service.database.PlayerService;

import java.io.File;
import java.sql.SQLException;
import java.util.Set;

public final class SPPlugin extends JavaPlugin {

    @Getter
    private static SPPlugin instance;
    @Getter
    private ConfigManager configManager;
    @Getter
    private FoliaLib foliaLib;
    private Database database;
    @Getter
    private PaymentService paymentService;
    @Getter
    private CommandHandler commandHandler;
    @Getter
    private CacheDataService cacheDataService;

    @Getter // TODO: Group this in one manager class
    private PaymentLogService paymentLogService;
    @Getter
    private PlayerService playerService;
    private boolean dev = false;
    @Getter
    private ViewFrame viewFrame;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        commandHandler = new CommandHandler(this);
        commandHandler.onLoad();
    }

    // TODO: A fking mess, please fix
    @Override
    public void onEnable() {
        // Reset config
        PacketEvents.getAPI().init();
        registerMetrics();

        // Thanks CHATGPT, qua met r
        OrderIDService.init(this);
        instance = this;
        foliaLib = new FoliaLib(this);
        // Plugin startup logic
        configManager = new ConfigManager(this);
        try {
            DatabaseSettings databaseConf = ConfigManager.getInstance().getConfig(DatabaseConfig.class);
            database = new Database(databaseConf);
        } catch (RuntimeException | SQLException e) {
            getLogger().warning("ChuyenXu failed to connect to database");
            this.getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        cacheDataService = new CacheDataService();
        new HookManager(this);
        playerService = new PlayerService(database.getPlayerDao());
        paymentLogService = new PaymentLogService(database);
        paymentService = new PaymentService();
        registerListener();
        commandHandler.onEnable();
        registerInventoryFramework();
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
                SuccessHandlingListener.class,
                SuccessDatabaseHandlingListener.class,
                PlayerJoinListener.class
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

    private void registerInventoryFramework() {
        viewFrame = ViewFrame.create(this)
                .install(AnvilInputFeature.AnvilInput)
                .with(new CardPINView(),
                        new CardSerialView(),
                        new CardListView(),
                        new CardPriceView(),
                        new PaymentHistoryView()
                )
                .disableMetrics()
                .register();
    }

    private void registerMetrics() {
        Metrics metrics = new Metrics(this, 25553);
        // check competitors stuff
        File dotManFolder = new File(getDataFolder().getParent(), "DotMan");
        File hmtopupFolder = new File(getDataFolder().getParent(), "HMTopUp");
        metrics.addCustomChart(new Metrics.SimplePie("had_dotman", () -> String.valueOf(dotManFolder.exists())));
        metrics.addCustomChart(new Metrics.SimplePie("had_hmtopup", () -> String.valueOf(hmtopupFolder.exists())));
    }

}
