package xyz.actrium.graves.menu.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.GraveManager;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.menu.Menu;
import xyz.actrium.graves.menu.MenuElement;
import xyz.actrium.graves.menu.MenuManager;

import java.util.ArrayList;
import java.util.List;

public class GraveMenu extends Menu {

    private final Death death;

    public GraveMenu(Death death) {
        super(54, Bukkit.getPlayer(death.getPlayerId()).getName() + "'s Grave");
        this.death = death;
    }

    @Override
    public List<MenuElement> getElements(Player viewer) {
        List<MenuElement> elements = new ArrayList<>();
        ItemStack[] armorContents = death.getArmorContents();
        ItemStack[] inventoryContents = death.getInventoryContents();

        elements.add(new MenuElement(45, armorContents[3], null));
        elements.add(new MenuElement(46, armorContents[2], null));
        elements.add(new MenuElement(47, armorContents[1], null));
        elements.add(new MenuElement(48, armorContents[0], null));

        elements.add(new MenuElement(49, death.getOffhandItem(), null));


        for (int i = 0; i < inventoryContents.length; i++) {
            ItemStack item = inventoryContents[i];

            if (item == null || item.getType().isAir()) {
                continue;
            }

            elements.add(new MenuElement(
                   i,
                    item,
                    null
            ));
        }

        ItemStack cancelItem = new ItemStack(Material.RED_STAINED_GLASS);
        ItemStack claimItem = new ItemStack(Material.GREEN_STAINED_GLASS);

        ItemMeta cancelMeta = cancelItem.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Close Menu");

        ItemMeta claimMeta = claimItem.getItemMeta();
        claimMeta.setDisplayName(ChatColor.GREEN + "Claim Grave");

        cancelItem.setItemMeta(cancelMeta);
        claimItem.setItemMeta(claimMeta);

        elements.add(new MenuElement(52, cancelItem, (player, event)
                -> ActriumGraves.get().menuManager.close(viewer)));

        elements.add(new MenuElement(53, claimItem, (player, event) -> {
            GraveManager.get().removeGrave(death);
            ActriumGraves.get().getItemHandler().removeItem(player);
            player.getInventory().setContents(death.getInventoryContents());
            player.getInventory().setArmorContents(death.getArmorContents());
            player.getInventory().setItemInOffHand(death.getOffhandItem());
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);

            ActriumGraves.get().menuManager.close(viewer);
        }));

        return elements;
    }
}
