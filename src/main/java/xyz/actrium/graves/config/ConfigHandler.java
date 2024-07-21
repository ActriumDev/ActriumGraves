package xyz.actrium.graves.config;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.impl.GraveConfig;
import xyz.actrium.graves.config.impl.GraveItemConfig;
import xyz.actrium.graves.config.impl.MessagesConfig;
import xyz.actrium.graves.config.impl.StorageConfig;
import xyz.actrium.graves.util.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;
    private FileConfiguration configuration;
    private final ActriumGraves plugin;

    public boolean checkForUpdates;

    private StorageConfig storageConfig;
    private GraveItemConfig graveItemConfig;
    private GraveConfig graveConfig;
    private MessagesConfig messagesConfig;


    public static ConfigHandler get() {
        return instance;
    }

    public ConfigHandler(ActriumGraves actriumGraveS) {
        this.plugin = actriumGraveS;
        instance = this;
    }

    public void init() {
        File file = new File(this.plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.plugin.logger.error("Config file is missing. Please restart your server to create it.");
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        } else {
            this.configuration = this.plugin.getConfig();
            this.storageConfig = new StorageConfig(this);
            this.graveItemConfig = new GraveItemConfig(this);
            this.graveConfig = new GraveConfig(this);
            this.messagesConfig = new MessagesConfig(this);
        }
    }

    public void load(Logger logger) {
        try {
            if (this.configuration == null) {
                return;
            }

            this.loadFields();
            logger.info("Config has been loaded");
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.logger.error("Failed to load configuration, please check the config.");
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        }

    }

    private void loadFields() {
        this.checkForUpdates = this.configuration.getBoolean("check-updates");
        this.storageConfig.loadFields();
        this.graveItemConfig.loadFields();
        this.graveConfig.loadFields();
        this.messagesConfig.loadFields();
    }

    public StorageConfig getStorageConfig() {
        return storageConfig;
    }

    public GraveItemConfig getGraveItemConfig() {
        return graveItemConfig;
    }

    public GraveConfig getGraveConfig() {
        return graveConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
