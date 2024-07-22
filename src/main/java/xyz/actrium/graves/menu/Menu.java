package xyz.actrium.graves.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {

    protected final Player player;

    protected Menu(Player player) {
        this.player = player;
    }

    public abstract List<ItemStack> getData();

    public abstract void handleClick(InventoryClickEvent event);

    protected void open() {
        Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.DARK_AQUA + "Your active graves");
        fillWithGlass(inventory);
        for (ItemStack data : getData()) {
            inventory.addItem(data);
        }
    }

    private void fillWithGlass(Inventory inventory) {
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        int size = inventory.getSize();
        int rows = size / 9;

        // Fill top and bottom rows
        for (int col = 0; col < 9; col++) {
            inventory.setItem(col, glassPane); // Top row
            inventory.setItem((rows - 1) * 9 + col, glassPane); // Bottom row
        }

        // Fill left and right columns
        for (int row = 0; row < rows; row++) {
            inventory.setItem(row * 9, glassPane); // Left column
            inventory.setItem(row * 9 + 8, glassPane); // Right column
        }
    }

}
