package xyz.actrium.graves.util;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import xyz.actrium.graves.ActriumGraves;

public class Logger {
    public final String PREFIX;
    public final ActriumGraves plugin;

    public Logger(ActriumGraves plugin) {
        this.PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "ActriumGraves" + ChatColor.GRAY + "] ";
        this.plugin = plugin;
    }

    public void noPrefix(String msg) {
        this.plugin.getServer().getConsoleSender().sendMessage(CC.translate(msg));
    }

    public void info(String msg) {
        this.plugin.getServer().getConsoleSender().sendMessage(this.PREFIX + ChatColor.GREEN + CC.translate(msg) + ChatColor.RESET);
    }

    public void warn(String msg) {
        this.plugin.getServer().getConsoleSender().sendMessage(this.PREFIX + ChatColor.YELLOW + CC.translate(msg) + ChatColor.RESET);
    }

    public void error(String msg) {
        this.plugin.getServer().getConsoleSender().sendMessage(this.PREFIX + ChatColor.RED + CC.translate(msg) + ChatColor.RESET);
    }
}