package xyz.actrium.graves.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.Permissions;
import xyz.actrium.graves.commands.SubCommand;
import xyz.actrium.graves.menu.impl.GraveReturnItemSelectMenu;

public class AdminReturnGraveCommand extends SubCommand {
    @Override
    public String getCommand() {
        return "returnitems";
    }

    @Override
    public String getDescription() {
        return "Deletes and returns a grave to the user it belongs to";
    }

    @Override
    public String getPermission() {
        return Permissions.ADMIN.asString();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length < 2) {
            player.sendMessage("§cUsage: /grave returnitems <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null || !target.isOnline()) {
            player.sendMessage("§cThat player is not online.");
            return;
        }

        ActriumGraves.get().menuManager.open(player, new GraveReturnItemSelectMenu(target));

    }
}
