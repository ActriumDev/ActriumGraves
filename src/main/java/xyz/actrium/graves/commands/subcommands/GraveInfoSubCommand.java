package xyz.actrium.graves.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.actrium.graves.Permissions;
import xyz.actrium.graves.commands.GraveCommand;
import xyz.actrium.graves.commands.SubCommand;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.util.CC;
import xyz.actrium.graves.util.TimeUtil;

public class GraveInfoSubCommand extends SubCommand {

    @Override
    public String getCommand() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Get information about a grave you are looking at.";
    }

    @Override
    public String getPermission() {
        return Permissions.INFO_COMMAND.asString();
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
            return;
        }

        sender.sendMessage(CC.CHAT_LINE);
        sender.sendMessage(ChatColor.GREEN + "Grave Owner: " + ChatColor.GRAY + Bukkit.getOfflinePlayer(grave.getPlayerId()).getName());
        sender.sendMessage(ChatColor.GREEN + "Grave Created: " + ChatColor.GRAY + TimeUtil.convertLongToReadableDate(grave.getTimeOfDeath()));
        sender.sendMessage(CC.CHAT_LINE);
    }
}
