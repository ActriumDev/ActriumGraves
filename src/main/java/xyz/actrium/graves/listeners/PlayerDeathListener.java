package xyz.actrium.graves.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.impl.GraveConfig;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.managers.GraveManager;

public class PlayerDeathListener implements Listener {
    private final GraveConfig graveConfig = ConfigHandler.get().getGraveConfig();

    private final List<Player> waitingDeathEvent = new ArrayList<>();


    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player)event.getEntity();

        World.Environment environment = player.getWorld().getEnvironment();
        if (!graveConfig.allowedDimensions.get(environment)) {
            player.sendMessage(ConfigHandler.get().getMessagesConfig().disabledDimension);
            return;
        }

        if (player.getHealth() - event.getFinalDamage() <= 0.0) {
            if (hasTotem(player)) return;
            try {
                if (GraveManager.get().getPlayerGraveCount(player) >= (long)this.graveConfig.maxGraves) {
                    player.sendMessage(ConfigHandler.get().getMessagesConfig().maxGraves);
                    return;
                }

                Death death = new Death(player);
                GraveManager.get().createGrave(player, death);
                waitingDeathEvent.add(player);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Sorry, seems like there was a error while spawning your grave. Your items were dropped normally.");
                waitingDeathEvent.remove(player);
            }
        }
    }

    @EventHandler
    public void onActualDeath(PlayerDeathEvent event) {
        if (waitingDeathEvent.contains(event.getEntity())) {
            event.getDrops().clear();
            waitingDeathEvent.remove(event.getEntity());
        }
    }

    public boolean hasTotem(Player player) {
        return player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
    }


}
