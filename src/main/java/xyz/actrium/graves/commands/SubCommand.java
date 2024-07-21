package xyz.actrium.graves.commands;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public abstract String getCommand();

    public abstract String getDescription();

    public String getPermission() {
        return null;
    }

    public boolean isPlayerOnly() {
        return false;
    }

    public String getArgMessage() {
        return this.getCommand();
    }

    public abstract void execute(CommandSender sender, String[] args);

    public List<String> getTabCompletions(List<String> tabs, CommandSender sender, String[] args) {
        return tabs;
    }

    protected final void sendInvalidMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Invalid Usage. /graves " + this.getArgMessage());
    }
}
