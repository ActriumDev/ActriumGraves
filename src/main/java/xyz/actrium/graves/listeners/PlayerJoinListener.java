package xyz.actrium.graves.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.actrium.graves.Permissions;
import xyz.actrium.graves.update.UpdateChecker;
import xyz.actrium.graves.util.CC;

public class PlayerJoinListener implements Listener {
    private final UpdateChecker updateChecker;

    public PlayerJoinListener(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.UPDATE_MESSAGE.asString())) {
            if (this.updateChecker.isFailed()) {
                player.sendMessage(CC.translate(CC.PREFIX + "&cFailed to check for an update, restart your server to fix this issue."));
                return;
            }

            if (this.updateChecker.isOutdated()) {
                player.sendMessage(CC.translate(CC.PREFIX + "&7A update for ActriumGraves is available for download. Latest version is &av" + this.updateChecker.getLatestVersion() + "&7."));
            }
        }

    }
}
