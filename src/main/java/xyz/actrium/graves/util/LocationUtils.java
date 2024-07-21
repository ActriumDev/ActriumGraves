package xyz.actrium.graves.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {
    public static String locationToString(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    public static Location locationFromString(String string) {
        String[] params = string.split(":");
        return new Location(Bukkit.getWorld(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), Double.parseDouble(params[3]));
    }

    public static boolean worldExists(String locString) {
        String[] params = locString.split(":");
        return Bukkit.getWorld(params[0]) != null;
    }
}
