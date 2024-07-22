package xyz.actrium.graves.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.actrium.graves.Permissions;
import xyz.actrium.graves.commands.GraveCommand;
import xyz.actrium.graves.commands.SubCommand;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.GraveManager;
import xyz.actrium.graves.util.LocationUtils;

public class AdminDeleteGraveSubCommand extends SubCommand {
    @Override
    public String getCommand() {
        return "admindelete";
    }

    @Override
    public String getDescription() {
        return "Deletes a grave you are currently looking at.";
    }

    @Override
    public String getPermission() {
        return Permissions.ADMIN_DELETE.asString();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            GraveCommand.sendUnknown(sender);
            return;
        }

        Player player = (Player)sender;
        Death grave = GraveCommand.getGraveLookingAt(player);

        if (grave == null) {
            player.sendMessage(ChatColor.RED + "You are not currently looking at a grave.");
        } else {
            GraveManager.get().removeGrave(grave);
            player.sendMessage(ChatColor.RED + "The grave at " + LocationUtils.locationToString(grave.getGraveLocation()) + " has been deleted!");
        }
    }
}