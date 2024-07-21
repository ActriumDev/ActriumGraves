package xyz.actrium.graves.config.impl;

import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.SectionConfig;
import xyz.actrium.graves.util.CC;

public class MessagesConfig extends SectionConfig {
    public String graveSpawn;
    public String wrongPlayer;
    public String graveDeleted;
    public String graveExpired;
    public String graveBreak;
    public String maxGraves;
    public String disabledDimension;

    public MessagesConfig(ConfigHandler configHandler) {
        super(configHandler.getConfiguration().getConfigurationSection("general-messages"));
    }

    @Override
    public void loadFields() {
        this.graveSpawn = CC.translate(this.section.getString("grave-spawn"));
        this.wrongPlayer = CC.translate(this.section.getString("wrong-player"));
        this.graveDeleted = CC.translate(this.section.getString("grave-deleted"));
        this.graveExpired = CC.translate(this.section.getString("grave-expired"));
        this.graveBreak = CC.translate(this.section.getString("break"));
        this.maxGraves = CC.translate(this.section.getString("max-graves"));
        this.disabledDimension = CC.translate(this.section.getString("dimension-disabled"));
    }
}
