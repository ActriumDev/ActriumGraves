package xyz.actrium.graves.commands;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.util.CC;

public class ActriumGravesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(CC.translate("&8&m========================"));
        sender.sendMessage(CC.translate("&7This server is running &bActriumGraves&7."));
        sender.sendMessage(CC.translate("&7 "));
        List<String> authors = Bukkit.getServer().getPluginManager().getPlugin(ActriumGraves.get().getName()).getDescription().getAuthors();
        sender.sendMessage(CC.translate("&7Developer&7: &b" + authors.get(0)));
        sender.sendMessage(CC.translate("&7Version&7: &bv" + ActriumGraves.get().version));
        sender.sendMessage(CC.translate("&8&m========================"));
        return true;
    }
}
