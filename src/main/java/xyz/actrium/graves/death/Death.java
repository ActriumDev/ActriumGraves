package xyz.actrium.graves.death;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.actrium.graves.util.BlockUtils;
import xyz.actrium.graves.util.LocationUtils;

public class Death {
    private final UUID playerId;
    private final Location deathLocation;
    private final Location graveLocation;
    private final long timeOfDeath;
    private final ItemStack[] inventoryContents;

    public Death(Player player) {
        this.playerId = player.getUniqueId();
        this.deathLocation = player.getLocation();
        this.graveLocation = BlockUtils.findGraveSpot(player.getLocation().getBlock().getLocation());
        this.timeOfDeath = System.currentTimeMillis();
        this.inventoryContents = player.getInventory().getContents();
    }

    public Death(Location graveLocation, Location deathLocation, UUID playerId, long timeOfDeath, ItemStack[] stacks) {
        this.graveLocation = graveLocation;
        this.deathLocation = deathLocation;
        this.playerId = playerId;
        this.timeOfDeath = timeOfDeath;
        this.inventoryContents = stacks;
    }



    public UUID getPlayerId() {
        return playerId;
    }

    public Location getGraveLocation() {
        return graveLocation;
    }

    public Location getDeathLocation() {
        return deathLocation;
    }

    public long getTimeOfDeath() {
        return timeOfDeath;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public String toString() {
        String returnString = LocationUtils.locationToString(this.graveLocation);
        return returnString + ", " + this.timeOfDeath;
    }
}
