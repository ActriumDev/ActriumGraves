package xyz.actrium.graves.listeners.block;

import java.util.Iterator;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.managers.GraveManager;

public class BlockBreakListeners implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (GraveManager.get().isGrave(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ActriumGraves.get().getConfigHandler().getMessagesConfig().graveBreak);
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        if (GraveManager.get().isGrave(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (GraveManager.get().isGrave(block.getLocation())) {
                event.blockList().remove(block);
            }
        }

    }

    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        if (GraveManager.get().isGrave(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}