package xyz.actrium.graves.menu.impl;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.GraveManager;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.menu.Menu;
import xyz.actrium.graves.menu.MenuElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GraveReturnItemSelectMenu extends Menu {

    private final Player playerToCheck;

    public GraveReturnItemSelectMenu(Player playerToCheck) {
        super(27, "Select a grave to return");
        this.playerToCheck = playerToCheck;
    }

    @Override
    public List<MenuElement> getElements(Player viewer) {
        List<MenuElement> elements = new ArrayList<>();
        List<Death> deaths;
        try {
            deaths = getNearbyDeaths(playerToCheck, viewer.getLocation(), 350);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        int slot = 0;

        for (Death death : deaths) {
            ItemStack head = createPlayerHead(playerToCheck, death);

            MenuElement element = new MenuElement(
                    slot, head, (clicker, event)
                    -> onPlayerSelected(clicker, death)
            );
            elements.add(element);
            slot++;
        }

        return elements;
    }

    private ItemStack createPlayerHead(Player player, Death death) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if (meta != null) {
            meta.setOwnerProfile(player.getPlayerProfile());

            meta.setDisplayName(ChatColor.GRAY + player.getName());

            Location graveLocation = death.getGraveLocation();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Position: " );
            lore.add(ChatColor.YELLOW + "X: " + ChatColor.RED + graveLocation.getBlockX());
            lore.add(ChatColor.YELLOW + "Y: " + ChatColor.RED + graveLocation.getBlockY());
            lore.add(ChatColor.YELLOW + "Z: " + ChatColor.RED + graveLocation.getBlockZ());

            meta.setLore(lore);
            item.setItemMeta(meta);

        }

        return item;
    }

    private void onPlayerSelected(
            Player viewer,
            Death data
    ) {
        viewer.sendMessage(ChatColor.GREEN + "Returning player item to " + playerToCheck.getName());

        GraveManager.get().removeGrave(data);
        ActriumGraves.get().getItemHandler().removeItem(playerToCheck);

        playerToCheck.getInventory().setContents(data.getInventoryContents());
        playerToCheck.getInventory().setArmorContents(data.getArmorContents());
        playerToCheck.getInventory().setItemInOffHand(data.getOffhandItem());


        ActriumGraves.get().menuManager.close(viewer);
    }

    private List<Death> getNearbyDeaths(Player playerToGetGraves, Location center, int radius) throws ExecutionException, InterruptedException {
        List<Death> nearby = new ArrayList<>();
        int radiusSquared = radius * radius;

        List<Death> playerGraves = GraveManager.get().getPlayerGraves(playerToGetGraves).get();

        for (Death death : playerGraves) {
            if (death.getGraveLocation().distanceSquared(center) <= radiusSquared) {
                nearby.add(death);
            }
        }
        return nearby;
    }
}
