package xyz.actrium.graves.util;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class StringUtils {

    public static String replacePlaceholder(String message, Player player, Location location) {
        String toReturn = message;
        if (message.contains("%distance%")) {
            int distance = (int)location.distance(player.getLocation());
            toReturn = message.replaceAll("%distance%", String.valueOf(distance));
        }

        return toReturn.replaceAll("%player%", player.getDisplayName())
                .replaceAll("%x%", String.valueOf(location.getBlockX()))
                .replaceAll("%y%", String.valueOf(location.getBlockY()))
                .replaceAll("%z%", String.valueOf(location.getBlockZ()));
    }

    public static List<String> replacePlaceholders(List<String> message, Player player, Location location) {
        for(int i = 0; i < message.size(); ++i) {
            String replaced = replacePlaceholder(message.get(i), player, location);
            message.remove(i);
            message.add(i, replaced);
        }

        return message;
    }
}
