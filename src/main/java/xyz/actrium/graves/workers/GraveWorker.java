package xyz.actrium.graves.workers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.managers.GraveManager;
import xyz.actrium.graves.util.StringUtils;

public class GraveWorker extends BukkitRunnable {
    private final GraveManager graveManager = GraveManager.get();

    @Override
    public void run() {
        if (ConfigHandler.get().getGraveConfig().deletionTime == 0) {
            this.cancel();
            return;
        }

        graveManager.graves.forEach((location, data) -> {
            long expirationTime = data.getTimeOfDeath() + (long)ConfigHandler.get().getGraveConfig().deletionTime * 1000L;

            if (System.currentTimeMillis() > expirationTime) {
                Player player = ActriumGraves.get().getServer().getPlayer(data.getPlayerId());
                if (player != null && player.isOnline()) {
                    String message = StringUtils.replacePlaceholder(ConfigHandler.get().getMessagesConfig().graveExpired, player, location);
                    player.sendMessage(message);
                    ActriumGraves.get().getItemHandler().removeItem(player, data);
                }

                graveManager.removeGrave(data);
            }

        });
    }
}
