package xyz.actrium.graves.menu;

import org.bukkit.inventory.ItemStack;

public class MenuElement {

    private final int slot;
    private final ItemStack item;
    private final ClickAction onClick;

    public MenuElement(
            int slot,
            ItemStack item,
            ClickAction onClick
    ) {
        this.slot = slot;
        this.item = item;
        this.onClick = onClick;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public ClickAction getOnClick() {
        return onClick;
    }
}
