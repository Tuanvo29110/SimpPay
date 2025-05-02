package org.simpmc.simppay;

import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpmc.simppay.api.DatabaseSettings;
import org.simpmc.simppay.commands.CommandHandler;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.DatabaseConfig;
import org.simpmc.simppay.database.Database;
import org.simpmc.simppay.listener.SimpPayListener;
import org.simpmc.simppay.service.PaymentService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
        commandHandler = new CommandHandler(this);
        commandHandler.onLoad();
    }

    @Override
    public void onEnable() {
        // Reset config
        if (dev) {
            try {
                File autocraftFolder = new File(getDataFolder().getParentFile(), getPluginMeta().getName());
                if (deleteDirectory(autocraftFolder)) {
                    getLogger().info("Successfully deleted plugins/autocraft.");
                } else {
                    getLogger().warning("plugins/autocraft did not exist or could not be deleted.");
                }
            } catch (IOException e) {
                getLogger().severe("Error deleting plugins/autocraft: " + e.getMessage());
            }
        }
        new Metrics(this, 25553);

        instance = this;
        foliaLib = new FoliaLib(this);
        // Plugin startup logic
        configManager = new ConfigManager(this);
        paymentService = new PaymentService();
        new SimpPayListener(this);
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
        if (database != null) {
            database.close();
        }
        commandHandler.onDisable();
        instance = null;
    }
    private boolean deleteDirectory(File dir) throws IOException {
        if (!dir.exists()) {
            return true;
        }

        File[] entries = dir.listFiles();
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    deleteDirectory(entry);
                } else {
                    if (!entry.delete()) {
                        throw new IOException("Failed to delete file: " + entry.getAbsolutePath());
                    }
                }
            }
        }

        if (!dir.delete()) {
            throw new IOException("Failed to delete directory: " + dir.getAbsolutePath());
        }

        return true;
    }

}
