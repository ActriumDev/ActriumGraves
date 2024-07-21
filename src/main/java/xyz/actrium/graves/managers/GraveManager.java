package xyz.actrium.graves.managers;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.config.impl.GraveConfig;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.storage.GraveStorage;
import xyz.actrium.graves.util.StringUtils;

public class GraveManager {
    public static final GraveConfig config = ConfigHandler.get().getGraveConfig();
    private static GraveManager instance;
    public ConcurrentHashMap<Location, Death> graves = new ConcurrentHashMap();
    private final ActriumGraves plugin;
    private final GraveStorage graveStorage;

    public static GraveManager get() {
        return instance == null ? new GraveManager(ActriumGraves.get()) : instance;
    }

    public GraveManager(ActriumGraves plugin) {
        instance = this;
        this.plugin = plugin;
        this.graveStorage = plugin.getStorageHandler().getGraveStorage();
        this.load();
    }

    public void load() {
        try {
            for (Death grave : this.graveStorage.getGraves()) {
                this.graves.put(grave.getGraveLocation(), grave);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        this.plugin.logger.info("Loaded " + this.graves.size() + " unclaimed graves in storage.");
    }

    public void createGrave(Player player, Death data) throws IOException {
        this.saveGrave(data);
        this.graves.put(data.getGraveLocation(), data);

        Location graveLocation = data.getGraveLocation().clone();
        this.createGraveBlocks(player, graveLocation);

        if (config.blockUnderGrave != Material.AIR) {
            graveLocation.clone().subtract(0.0, 1.0, 0.0).getBlock().setType(config.blockUnderGrave);
        }

        player.sendMessage(StringUtils.replacePlaceholder(ConfigHandler.get().getMessagesConfig().graveSpawn, player, graveLocation));
    }

    public void removeGrave(Death data) {
        Block graveBlock = data.getGraveLocation().getBlock();
        Block signBlock = data.getGraveLocation().clone().add(1.0, 0.0, 0.0).getBlock();
        graveBlock.setType(Material.AIR);
        if (signBlock.getType() == Material.OAK_SIGN) {
            signBlock.setType(Material.AIR);
        }

        this.graveStorage.removeGrave(data);
        this.graves.remove(data.getGraveLocation());
    }

    public void saveGrave(Death data) throws IOException {
        this.graveStorage.saveGrave(data);
    }

    private void createGraveBlocks(Player player, Location graveLoc) {
        graveLoc.getBlock().setType(Material.PLAYER_HEAD);

        Skull skull = (Skull)graveLoc.getBlock().getState();
        skull.setOwnerProfile(player.getPlayerProfile());
        skull.update();

        if (config.graveSign) {
            Location signLoc = graveLoc.clone().add(1.0, 0.0, 0.0);
            signLoc.getBlock().setType(Material.OAK_SIGN);
            Sign sign = (Sign)signLoc.getBlock().getState();

            for(int index = 0; index < config.graveSignContents.size(); ++index) {
                String graveSignContent = config.graveSignContents.get(index);
                String lineContent = StringUtils.replacePlaceholder(graveSignContent, player, graveLoc);
                sign.setLine(index, lineContent);
            }

            sign.update();
        }

    }

    public boolean isGrave(Location lastDeathLoc) {
        return this.graves.containsKey(lastDeathLoc.getBlock().getLocation());
    }

    public Death getGrave(Location location) {
        return graves.get(location);
    }

    public long getPlayerGraveCount(Player player) {
        return this.graves.values().stream().filter((data) -> data.getPlayerId().toString().equals(player.getUniqueId().toString())).count();
    }

    public CompletableFuture<Death> getLastGrave(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Death> playerGraves = this.getPlayerGraves(player).get();
                playerGraves.sort((o1, o2) -> Long.compare(o2.getTimeOfDeath(), o1.getTimeOfDeath()));
                return playerGraves.stream().findFirst().orElse(null);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<List<Death>> getPlayerGraves(Player player) {
        return CompletableFuture.supplyAsync(() -> this.graves.values().stream().filter((data)
                -> data.getPlayerId().toString().equals(player.getUniqueId().toString())).collect(Collectors.toList()));
    }
}
