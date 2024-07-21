package xyz.actrium.graves.listeners;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.impl.GraveConfig;
import xyz.actrium.graves.config.impl.MessagesConfig;
import xyz.actrium.graves.cooldown.BasicCooldown;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.managers.GraveManager;
import xyz.actrium.graves.util.StringUtils;

public class GraveInteractListener implements Listener {
    private final MessagesConfig messagesConfig = ConfigHandler.get().getMessagesConfig();
    private final GraveConfig graveConfig = ConfigHandler.get().getGraveConfig();
    private final BasicCooldown basicCooldown = new BasicCooldown(2000);

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (this.basicCooldown.onCooldown(player)) return;
            if (!GraveManager.get().isGrave(event.getClickedBlock().getLocation())) return;

            this.basicCooldown.updatePlayer(player);

            Block graveBlock = event.getClickedBlock();
            Death data = GraveManager.get().getGrave(graveBlock.getLocation());

            if (data == null) {
                player.sendMessage(this.messagesConfig.graveDeleted);
                return;
            }

            if (!player.getUniqueId().equals(data.getPlayerId()) && this.graveConfig.privateGraves) {
                Player toBelongTo = Bukkit.getPlayer(data.getPlayerId());
                player.sendMessage(StringUtils.replacePlaceholder(this.messagesConfig.wrongPlayer, toBelongTo, data.getGraveLocation()));
                return;
            }

            GraveManager.get().removeGrave(data);

            ItemStack[] contents = data.getInventoryContents();
            int size = contents.length;
            for(int i = 0; i < size; ++i) {
                ItemStack content = contents[i];
                if (content != null) {
                    player.getWorld().dropItemNaturally(data.getGraveLocation(), content);
                }
            }

            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
            ActriumGraves.get().getItemHandler().removeItem(player);

        }
    }
}