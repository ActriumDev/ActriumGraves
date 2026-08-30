package xyz.actrium.graves.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private final int size;
    private final String title;

    protected Menu(
            int size,
            String title
    ) {
        if (size % 9 != 0) {
            throw new IllegalArgumentException(
                    "Inventory size must be divisible by 9."
            );
        }

        if (size < 9 || size > 54) {
            throw new IllegalArgumentException(
                    "Inventory size must be between 9 and 54."
            );
        }

        this.size = size;
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public Inventory createInventory(Player viewer) {
        Inventory inventory = Bukkit.createInventory(
                null,
                size,
                title
        );

        for (MenuElement element : getElements(viewer)) {
            if (element.getSlot() < 0 ||
                    element.getSlot() >= size) {
                continue;
            }

            inventory.setItem(
                    element.getSlot(),
                    element.getItem()
            );
        }

        return inventory;
    }

    public List<MenuElement> getElements(Player viewer) {
        return new ArrayList<>();
    }

    public void onOpen(Player viewer) {
    }

    public void onClose(Player viewer) {
    }
}
