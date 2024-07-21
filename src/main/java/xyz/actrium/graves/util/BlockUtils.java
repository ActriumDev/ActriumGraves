package xyz.actrium.graves.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockUtils {
    public static boolean isAir(Block block) {
        return block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR || block.getType() == Material.VOID_AIR;
    }

    public static Location findGraveSpot(Location location) {
        // add 5 to y if in end spawn due to spawn regen
        if (location.getWorld().getEnvironment() == World.Environment.THE_END) {
            if (location.getWorld().getSpawnLocation().distance(location) <= 5) {
                location.add(0.0, 5.0f, 0.0);
            }
        }
        while(!isAir(location.getBlock())) {
            location.add(0.0, 1.0, 0.0);
        }

        return location;
    }
}