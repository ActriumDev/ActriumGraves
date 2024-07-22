package xyz.actrium.graves.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuManager {

    private HashMap<Player, Menu> openMenus = new HashMap<>();

    public void openMenu(Player player, Menu menu) {
        openMenus.put(player, menu);
        menu.open();
    }

    public void handleClose(Player player, InventoryCloseEvent event) {
        Menu menu = openMenus.get(player);
        if (menu == null) return;

        openMenus.remove(player);
    }

    public void handleMenuClick(Player player, InventoryClickEvent event) {
        Menu menu = openMenus.get(player);
        if (menu == null) return;

        event.setCancelled(true);
        menu.handleClick(event);
    }

}
