package xyz.actrium.graves.config.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.SectionConfig;
import xyz.actrium.graves.util.CC;

public class GraveItemConfig extends SectionConfig {
    public boolean enabled;
    public boolean removeItem;
    public boolean itemClick;
    public boolean compassPoint;
    public Material itemType;
    public String itemName;
    public String clickmessage;
    public List<String> bookContents;
    public List<String> itemLore;

    public GraveItemConfig(ConfigHandler configHandler) {
        super(configHandler.getConfiguration().getConfigurationSection("grave-item"));
    }

    @Override
    public void loadFields() {
        this.enabled = this.section.getBoolean("enabled");
        Material material = Material.getMaterial(this.section.getString("item-type").toUpperCase());
        if (material == null && !material.isItem()) {
            ActriumGraves.get().logger.error(this.section.getString("item-type") + " is not a valid item, make sure you are using bukkit/spigot names.");
            ActriumGraves.get().disable();
            return;
        }

        this.itemType = material;
        this.itemClick = this.section.getBoolean("item-click");
        this.itemName = CC.translate(this.section.getString("item-name"));

        this.itemLore = new ArrayList();
        for (String s : this.section.getStringList("item-lore")) {
            this.itemLore.add(CC.translate(s));
        }

        this.clickmessage = CC.translate(this.section.getString("item-click-message"));
        this.removeItem = this.section.getBoolean("remove-item");
        this.compassPoint = this.section.getBoolean("compass-point");
        this.bookContents = this.section.getStringList("book-contents");
    }
}
