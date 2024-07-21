package xyz.actrium.graves.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.actrium.graves.death.Death;

public abstract class GraveItem {
    public abstract ItemStack newItem(Player player, Death data);

    public abstract void onRightClick(Player player, Death data, ItemStack itemStack);

    public abstract void onLeftClick(Player player, Death data, ItemStack itemStack);
}
