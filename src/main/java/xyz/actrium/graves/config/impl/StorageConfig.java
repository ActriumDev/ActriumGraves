package xyz.actrium.graves.config.impl;

import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.SectionConfig;
import xyz.actrium.graves.storage.StorageType;

public class StorageConfig extends SectionConfig {
    public StorageType type;
    public String host;
    public String dbName;
    public String username;
    public String password;
    public int port;
    public boolean authed;

    public StorageConfig(ConfigHandler configHandler) {
        super(configHandler.getConfiguration().getConfigurationSection("storage-info"));
    }

    @Override
    public void loadFields() {
        String typeString = this.section.getString("type");
        if (!StorageType.valid(typeString)) {
            ActriumGraves.get().logger.error(typeString + " is not a valid storage type!");
            ActriumGraves.get().disable();
            return;
        }

        this.type = StorageType.valueOf(typeString.toUpperCase());
        this.host = this.section.getString("host");
        this.port = this.section.getInt("port");
        this.dbName = this.section.getString("dbName");
        this.authed = this.section.getBoolean("auth.authed");
        this.username = this.section.getString("auth.username");
        this.password = this.section.getString("auth.password");
    }
}
