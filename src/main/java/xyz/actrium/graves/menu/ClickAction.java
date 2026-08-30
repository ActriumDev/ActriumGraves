package xyz.actrium.graves.menu;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ClickAction {

    void execute(
            Player viewer,
            InventoryClickEvent event
    );
}
