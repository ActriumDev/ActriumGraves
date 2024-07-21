package xyz.actrium.graves.managers;

import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.impl.GraveItemConfig;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.item.GraveItem;
import xyz.actrium.graves.item.impl.BookGraveItem;
import xyz.actrium.graves.item.impl.CompassGraveItem;
import xyz.actrium.graves.util.LocationUtils;
import xyz.actrium.graves.util.StringUtils;

public class ItemManager {
    public static NamespacedKey ITEM_KEY = new NamespacedKey(ActriumGraves.get(), "grave-item");
    public static NamespacedKey GRAVE_KEY = new NamespacedKey(ActriumGraves.get(), "grave-location");
    public static NamespacedKey DEATH_KEY = new NamespacedKey(ActriumGraves.get(), "death-location");


    public GraveItemConfig config = ConfigHandler.get().getGraveItemConfig();
    public CompassGraveItem compassGraveItem = new CompassGraveItem();
    public BookGraveItem bookGraveItem = new BookGraveItem();

    public void givePlayerItem(Player player, Death data) {
        if (this.getFunctionalItem() == null) {
            player.getInventory().addItem(createBasicItem(player, data));
            return;
        }

        GraveItem graveItem = this.getFunctionalItem();
        player.getInventory().addItem(graveItem.newItem(player, data));
    }

    public void handleLeftClick(Player player, Death data, ItemStack itemStack) {
        GraveItem item = this.getFunctionalItem(itemStack);
        if (item != null) {
            item.onLeftClick(player, data, itemStack);
        }
    }

    public void handleRightClick(Player player, Death data, ItemStack itemStack) {
        GraveItem item = this.getFunctionalItem(itemStack);
        if (item != null) {
            item.onRightClick(player, data, itemStack);
        }
    }

    public ItemStack createBasicItem(Player player, Death data) {
        ItemStack stack = new ItemStack(this.config.itemType);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(StringUtils.replacePlaceholder(this.config.itemName, player, data.getGraveLocation()));
        meta.setLore(StringUtils.replacePlaceholders(this.config.itemLore, player, data.getGraveLocation()));

        stack.setItemMeta(meta);
        return stack;
    }

    public void removeItem(Player player) {
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getItemMeta() == null) return;
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_KEY, PersistentDataType.BOOLEAN)) return;

            player.getInventory().remove(itemStack);
        }
    }

    public void removeItem(Player player, Death data) {

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getItemMeta() == null) return;
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_KEY, PersistentDataType.BOOLEAN)) return;

            String graveKey = itemStack.getItemMeta().getPersistentDataContainer().get(GRAVE_KEY, PersistentDataType.STRING);
            if (LocationUtils.locationFromString(graveKey).equals(data.getGraveLocation())) {
                player.getInventory().remove(itemStack);
                break;
            }
        }

    }

    public static boolean isFunctionalItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pData = meta.getPersistentDataContainer();
        return pData.has(ITEM_KEY, PersistentDataType.BOOLEAN);
    }

    public static Location getGraveLocation(ItemStack stack) {
        return LocationUtils.locationFromString(stack.getItemMeta().getPersistentDataContainer().get(GRAVE_KEY, PersistentDataType.STRING));
    }

    public GraveItem getFunctionalItem() {
        return switch (this.config.itemType) {
            case COMPASS -> this.compassGraveItem;
            case BOOK -> this.bookGraveItem;
            default -> null;
        };
    }

    public GraveItem getFunctionalItem(ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(ITEM_KEY, PersistentDataType.BOOLEAN)) return null;

        return switch (itemStack.getType()) {
            case COMPASS -> this.compassGraveItem;
            case BOOK -> this.bookGraveItem;
            default -> null;
        };
    }
}
