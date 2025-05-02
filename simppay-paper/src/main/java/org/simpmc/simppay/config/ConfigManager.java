package org.simpmc.simppay.config;

import de.exlll.configlib.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.serializers.KeySerializer;
import org.simpmc.simppay.config.serializers.SoundComponentSerializer;
import org.simpmc.simppay.config.serializers.TextComponentSerializer;
import org.simpmc.simppay.config.types.*;
import org.simpmc.simppay.config.types.banking.PayosConfig;
import org.simpmc.simppay.config.types.card.ThesieutocConfig;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    // holds loaded config instances
    public static final Map<Class<?>, Object> configs = new HashMap<>();
    private final SPPlugin plugin;
    private final List<Class<?>> configClasses = List.of(
            BankingConfig.class,
            CardConfig.class,
            DatabaseConfig.class,
            MainConfig.class,
            MessageConfig.class,
            ThesieutocConfig.class,
            PayosConfig.class
    );
    // holds file paths for each config type
    private final Map<Class<?>, Path> configPaths = new HashMap<>();
    private final YamlConfigurationProperties properties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
            .addSerializer(Key.class, new KeySerializer())
            .addSerializer(Sound.class, new SoundComponentSerializer())
            .addSerializer(Component.class, new TextComponentSerializer())
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .header("""
                    SimpPay @ 2025
                    Made by typical.smc, used for SimpMC Network
                    Shared publicly on github.com/SimpMC_Network/SimpPay
                    """)
            .header("Discord: discord.gg/simpmc")
            .build();

    public ConfigManager(SPPlugin plugin) {
        this.plugin = plugin;
        // build default YAML properties
        // prepare paths and load all
        initPaths();
        registerAll();
    }

    private static <T> T makeNew(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create config instance for " + clazz.getName(), e);
        }
    }

    private void initPaths() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        for (Class<?> cls : configClasses) {
            configPaths.put(cls, getConfigPath(cls.getSimpleName()));
        }
    }

    @SuppressWarnings("unchecked")
    private void registerAll() {
        plugin.getLogger().info("Loading all configurations");
        for (Class<?> rawClass : configClasses) {
            // capture wildcard and process each
            registerConfig((Class<Object>) rawClass);
        }
        plugin.getLogger().info("All configurations loaded successfully");
    }

    private <T> void registerConfig(Class<T> cfgClass) {
        Path path = configPaths.get(cfgClass);
        File file = path.toFile();

        YamlConfigurationStore<T> store = new YamlConfigurationStore<>(cfgClass, properties);

        store.update(path);


        // load (with properties on first load)
        T loaded = store.load(path);
        configs.put(cfgClass, loaded);
    }

    /**
     * Reload all configs (e.g. on /simppayadmin reload)
     */
    public void reloadAll() {
        configs.clear();
        plugin.getLogger().info("Reloading all configurations");
        for (Class<?> rawClass : configClasses) {
            registerConfig(rawClass);
        }
        plugin.getLogger().info("All configurations reloaded successfully");
    }

    /**
     * Retrieve a loaded configuration instance by its class
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(Class<T> cls) {
        return (T) configs.get(cls);
    }

    private Path getConfigPath(String name) {
        String fileName = getConfigFileName(name) + ".yml";
        return Paths.get(plugin.getDataFolder().getPath(), fileName);
    }

    private String getConfigFileName(String name) {
        var builder = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) builder.append('-');
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
