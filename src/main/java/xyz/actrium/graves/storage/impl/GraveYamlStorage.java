package xyz.actrium.graves.storage.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.storage.GraveStorage;
import xyz.actrium.graves.util.ItemUtils;
import xyz.actrium.graves.util.LocationUtils;

public class GraveYamlStorage implements GraveStorage {
    public FileConfiguration config;
    public File file;
    public ConfigurationSection mainSection;

    @Override
    public void init(ActriumGraves plugin) {
        try {
            this.file = new File(plugin.getServer().getWorlds().get(0).getWorldFolder(), "graves.yml");
            if (!this.file.exists()) {
                this.file.createNewFile();
                ActriumGraves.get().logger.info("Created the YML graves file in the main world folder.");
            }

            this.config = new YamlConfiguration();
            this.config.load(this.file);
            if (!this.config.isConfigurationSection("graves")) {
                this.mainSection = this.config.createSection("graves");
            } else {
                this.mainSection = this.config.getConfigurationSection("graves");
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Death> getGraves() {
        ArrayList<Death> toReturn = new ArrayList();

        for (String key : this.mainSection.getKeys(false)) {
            ConfigurationSection sec = this.config.getConfigurationSection("graves." + key);

            Location graveLoc = LocationUtils.locationFromString(sec.getName());
            Location actualDeathLocation = LocationUtils.locationFromString(sec.getString("actual-death"));

            UUID uuid = UUID.fromString(sec.getString("uuid"));
            long created = sec.getLong("created");

            try {
                ItemStack[] items = ItemUtils.convertStringToitems(sec.getString("contents"));
                Death death = new Death(graveLoc, actualDeathLocation, uuid, created, items);
                toReturn.add(death);
            } catch (ClassNotFoundException | IOException e) {
                ActriumGraves.get().logger.error("Failed to get grave contents for UUID " + uuid);
                e.printStackTrace();
            }
        }

        return toReturn;
    }

    @Override
    public void saveGrave(Death data) throws IOException {
        ConfigurationSection sec = this.config.createSection("graves." + LocationUtils.locationToString(data.getGraveLocation()));
        sec.set("uuid", data.getPlayerId().toString());
        sec.set("actual-death", LocationUtils.locationToString(data.getDeathLocation()));
        sec.set("created", data.getTimeOfDeath());
        sec.set("contents", ItemUtils.convertItemsToString(data.getInventoryContents()));
        this.save();
    }

    @Override
    public void removeGrave(Death data) {
        this.mainSection.set(LocationUtils.locationToString(data.getGraveLocation()), null);
        this.save();
    }

    private void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
