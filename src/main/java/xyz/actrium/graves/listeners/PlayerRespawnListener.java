package xyz.actrium.graves.listeners;

import java.util.concurrent.ExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.GraveManager;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!ConfigHandler.get().getGraveItemConfig().enabled) return;

        Location lastDeathLoc = event.getPlayer().getLastDeathLocation();
        if (lastDeathLoc == null) return;

        try {
            Death data = GraveManager.get().getLastGrave(event.getPlayer()).get();
            if (data == null) {
                return;
            }

            ActriumGraves.get().getItemHandler().givePlayerItem(event.getPlayer(), data);
        } catch (InterruptedException | ExecutionException e) {
            event.getPlayer().sendMessage(ChatColor.RED + "An error accord while giving you the grave location compass.");
            e.printStackTrace();
        }
    }
}
