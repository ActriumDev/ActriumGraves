package xyz.actrium.graves.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.cooldown.BasicCooldown;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.managers.ItemManager;

public class ItemUseListener implements Listener {
    private final BasicCooldown basicCooldown = new BasicCooldown(3500);

    @EventHandler
    public void useItem(PlayerInteractEvent event) {
        if (!ConfigHandler.get().getGraveItemConfig().enabled) return;
        Player player = event.getPlayer();

        if (this.basicCooldown.onCooldown(player)) return;

        ItemStack stack = event.getItem();
        if (stack == null) return;
        this.basicCooldown.updatePlayer(player);

        if (!ItemManager.isFunctionalItem(stack)) return;


        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            ActriumGraves.get().getItemHandler().handleLeftClick(player, null, stack);
        }else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getAction() == Action.RIGHT_CLICK_AIR) {
            ActriumGraves.get().getItemHandler().handleRightClick(player, null, stack);
        }
    }
}