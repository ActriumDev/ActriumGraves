package xyz.actrium.graves.menu.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.GraveManager;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.item.ItemManager;
import xyz.actrium.graves.menu.Menu;
import xyz.actrium.graves.util.CC;
import xyz.actrium.graves.util.StringUtils;
import xyz.actrium.graves.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

public class GravesMenu extends Menu {

    private final static NamespacedKey KEY = new NamespacedKey(ActriumGraves.get(), "gravesMenu");

    private HashMap<ItemStack, Death> deathsToItems;

    protected GravesMenu(Player player) {
        super(player);
        deathsToItems = new HashMap<>();
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
                // TODO: configuration
                lore.add(ChatColor.WHITE + "Distance: " + distance + " blocks");
                lore.add(ChatColor.WHITE + "Time Till Deletion: " + TimeUtil.convertLongToRemainingDuration(death.getTimeOfDeath())); // TODO: Get time till deletion
                lore.add(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━");
                // TODO: configuration
                lore.add(ChatColor.RED + "Right Click to delete.");
                lore.add(ChatColor.RED + "Left Click to teleport.");
                meta.setLore(lore);

                stack.setItemMeta(meta);
                deathsToItems.put(stack, death);
                return stack;
            }).toList();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        ItemStack clickItem = event.getCurrentItem();
        if (clickItem == null || clickItem.getType() != Material.ENDER_EYE) return;

        Death death = deathsToItems.get(clickItem);
        if (death == null) {
            player.sendMessage(CC.translate("&cSomething has went wrong, please try again."));
            return;
        }

        Player player = (Player) event.getWhoClicked();

        // handle left click
        if (event.isLeftClick()) {

            // TODO: configuration
            player.teleport(death.getGraveLocation());
            player.sendMessage(ChatColor.YELLOW + "Teleporting to grave...");
            player.closeInventory();

        }
        else if (event.isRightClick()) {

            // TODO: configuration
            player.sendMessage(ChatColor.RED + "Deleting this grave...");
            GraveManager.get().removeGrave(death);
            player.closeInventory();

        }

    }
}
