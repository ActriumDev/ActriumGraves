package xyz.actrium.graves.item.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.impl.GraveItemConfig;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.item.GraveItem;
import xyz.actrium.graves.item.ItemManager;
import xyz.actrium.graves.util.LocationUtils;
import xyz.actrium.graves.util.StringUtils;

public class CompassGraveItem extends GraveItem {
    private final GraveItemConfig config = ConfigHandler.get().getGraveItemConfig();

    @Override
    public ItemStack newItem(Player player, Death data) {
        ItemStack stack = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta)stack.getItemMeta();

        meta.setLodestone(data.getGraveLocation());
        meta.setLodestoneTracked(false);

        meta.setDisplayName(StringUtils.replacePlaceholder(this.config.itemName, player, data.getGraveLocation()));
        meta.setLore(StringUtils.replacePlaceholders(this.config.itemLore, player, data.getGraveLocation()));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(ItemManager.ITEM_KEY, PersistentDataType.BOOLEAN, true);
        container.set(ItemManager.GRAVE_KEY, PersistentDataType.STRING, LocationUtils.locationToString(data.getGraveLocation()));
        container.set(ItemManager.DEATH_KEY, PersistentDataType.STRING, LocationUtils.locationToString(data.getDeathLocation()));

        stack.addUnsafeEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void onRightClick(Player player, Death data, ItemStack itemStack) {
        if (!ConfigHandler.get().getGraveItemConfig().itemClick) return;
        Location loc = ItemManager.getGraveLocation(itemStack);

        String message = ActriumGraves.get().getConfigHandler().getGraveItemConfig().clickmessage;
        message = StringUtils.replacePlaceholder(message, player, loc);

        if (message.startsWith("/")) {
            player.chat(message);
        } else {
            player.sendMessage(message);
        }
    }

    @Override
    public void onLeftClick(Player player, Death data, ItemStack itemStack) {
        if (!ConfigHandler.get().getGraveItemConfig().itemClick) return;
        Location loc = ItemManager.getGraveLocation(itemStack);

        String message = ActriumGraves.get().getConfigHandler().getGraveItemConfig().clickmessage;
        message = StringUtils.replacePlaceholder(message, player, loc);

        if (message.startsWith("/")) {
            player.chat(message);
        } else {
            player.sendMessage(message);
        }
    }
}
