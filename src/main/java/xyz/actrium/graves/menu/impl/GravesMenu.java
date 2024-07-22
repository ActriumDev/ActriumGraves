package xyz.actrium.graves.menu.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.actrium.graves.GraveManager;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.item.ItemManager;
import xyz.actrium.graves.menu.Menu;
import xyz.actrium.graves.util.StringUtils;
import xyz.actrium.graves.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

public class GravesMenu extends Menu {
    protected GravesMenu(Player player) {
        super(player);
    }

    @Override
    public List<ItemStack> getData() {
        try {
            List<Death> deaths = GraveManager.get().getPlayerGraves(player).get();
            deaths.sort((o1, o2) -> Long.compare(o2.getTimeOfDeath(), o1.getTimeOfDeath()));

            return deaths.stream().map(death -> {
                ItemStack stack = new ItemStack(Material.ENDER_EYE);
                ItemMeta meta = stack.getItemMeta();

                String time = TimeUtil.convertLongToReadableDate(death.getTimeOfDeath());
                meta.setDisplayName(ChatColor.RED + time);
                ArrayList<String> lore = new ArrayList<>();
                int distance = (int)death.getGraveLocation().distance(player.getLocation());
                lore.add(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━");
                lore.add(ChatColor.WHITE + "Distance: " + distance + " blocks");
                lore.add(ChatColor.WHITE + "Time Till Deletion: " + TimeUtil.convertLongToRemainingDuration(death.getTimeOfDeath())); // TODO: Get time till deletion
                lore.add(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━");

                meta.setLore(lore);

                stack.setItemMeta(meta);
                return stack;
            }).toList();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        if (event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        ItemStack clickItem = event.getCurrentItem();

        // TODO: get grave from this item


    }
}
