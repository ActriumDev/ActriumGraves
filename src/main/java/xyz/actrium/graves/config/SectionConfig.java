package xyz.actrium.graves.config;

import org.bukkit.configuration.ConfigurationSection;

public abstract class SectionConfig {
    public final ConfigurationSection section;

    public SectionConfig(ConfigurationSection section) {
        this.section = section;
    }

    public abstract void loadFields();
}
