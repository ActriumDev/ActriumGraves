package xyz.actrium.graves.config.impl;

import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.World;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.SectionConfig;
import xyz.actrium.graves.util.GraveType;

public class GraveConfig extends SectionConfig {
    public GraveType graveType;
    public boolean privateGraves;
    public boolean graveSign;
    public int maxGraves;
    public int deletionTime;
    public Material blockUnderGrave;
    public List<String> graveSignContents;

    public HashMap<World.Environment, Boolean> allowedDimensions;

    public GraveConfig(ConfigHandler handler) {
        super(handler.getConfiguration().getConfigurationSection("grave"));
    }

    @Override
    public void loadFields() {
        String graveTypeString = this.section.getString("grave-type");
        if (!GraveType.valid(graveTypeString)) {
            ActriumGraves.get().logger.error(graveTypeString + " is not a valid grave type.");
            ActriumGraves.get().disable();
        }

        this.graveType = GraveType.valueOf(graveTypeString.toUpperCase());
        this.privateGraves = this.section.getBoolean("private");
        this.maxGraves = this.section.getInt("max-allowed");
        Material material = Material.getMaterial(this.section.getString("grave-under-block").toUpperCase());

        if (material == null || !material.isBlock()) {
            ActriumGraves.get().logger.error(this.section.getString("grave-under-block") + " is not a valid block, make sure you are using bukkit/spigot names.");
            ActriumGraves.get().disable();
            return;
        }

        this.blockUnderGrave = material;
        this.deletionTime = this.section.getInt("delete-time");
        this.graveSign = this.section.getBoolean("grave-sign");
        this.graveSignContents = this.section.getStringList("grave-sign-contents");

        this.allowedDimensions = new HashMap<>();
        this.allowedDimensions.put(World.Environment.NORMAL, this.section.getBoolean("allowed-dimensions.overworld"));
        this.allowedDimensions.put(World.Environment.NETHER, this.section.getBoolean("allowed-dimensions.nether"));
        this.allowedDimensions.put(World.Environment.THE_END, this.section.getBoolean("allowed-dimensions.end"));
        this.allowedDimensions.put(World.Environment.CUSTOM, this.section.getBoolean("allowed-dimensions.other"));

    }
}