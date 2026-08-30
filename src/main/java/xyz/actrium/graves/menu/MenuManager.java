package xyz.actrium.graves.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager implements Listener {

    private final Map<UUID, Menu> openUIs = new HashMap<>();
    private final Map<UUID, Map<Integer, MenuElement>> elements = new HashMap<>();

    public MenuManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player player, Menu ui) {

        Inventory inventory = ui.createInventory(player);

        Map<Integer, MenuElement> elementMap = new HashMap<>();

        ui.getElements(player).forEach(e -> {
            elementMap.put(e.getSlot(), e);
        });

        openUIs.put(player.getUniqueId(), ui);
        elements.put(player.getUniqueId(), elementMap);

        player.openInventory(inventory);

        ui.onOpen(player);
    }

    public void close(Player player) {
        Menu ui = openUIs.remove(player.getUniqueId());
        elements.remove(player.getUniqueId());

        if (ui != null) {
            ui.onClose(player);
        }

        player.closeInventory();
    }

    public void refresh(Player player) {
        Menu ui = openUIs.get(player.getUniqueId());

        if (ui == null) return;

        open(player, ui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Menu ui = openUIs.get(player.getUniqueId());
        if (ui == null) return;

        event.setCancelled(true);

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        int slot = event.getSlot();

        Map<Integer, MenuElement> elementMap = elements.get(player.getUniqueId());
        if (elementMap == null) return;

        MenuElement element = elementMap.get(slot);
        if (element == null) return;

        ClickAction action = element.getOnClick();
        if (action == null) return;

        action.execute(player, event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!openUIs.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        Menu ui = openUIs.remove(player.getUniqueId());
        elements.remove(player.getUniqueId());

        if (ui != null) {
            ui.onClose(player);
        }
    }

    public void closeAll() {
        for (UUID uuid : openUIs.keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) player.closeInventory();
        }

        openUIs.clear();
        elements.clear();
    }

    public Menu getOpenUI(Player player) {
        return openUIs.get(player.getUniqueId());
    }

    public boolean hasUIOpen(Player player) {
        return openUIs.containsKey(player.getUniqueId());
    }




}
