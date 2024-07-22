package xyz.actrium.graves.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import xyz.actrium.graves.menu.MenuManager;

public class MenuListeners implements Listener {

    private final MenuManager menuManager;

    public MenuListeners(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        menuManager.handleClose((Player) event.getPlayer(), event);
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        menuManager.handleMenuClick((Player) event.getWhoClicked(), event);
    }

}
