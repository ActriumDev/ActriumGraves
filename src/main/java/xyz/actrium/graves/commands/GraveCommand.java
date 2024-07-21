package xyz.actrium.graves.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Warning;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.managers.GraveManager;
import xyz.actrium.graves.util.CC;

public class GraveCommand implements CommandExecutor, TabCompleter {
    public List<SubCommand> subCommands = new ArrayList<>();

    public void registerSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0 && !args[0].equalsIgnoreCase("help")) {
            SubCommand subCommand = this.getSubCommand(args[0]);
            if (subCommand == null) {
                sender.sendMessage(ChatColor.RED + "Unknown command. Do \"/grave help\" to see all available commands!");
                return false;
            } else {

                boolean isPlayer = sender instanceof Player;

                if (subCommand.isPlayerOnly() && !isPlayer) {
                    sender.sendMessage(ChatColor.RED + "Only players can run this command!");
                    return true;
                }

                if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                    sendUnknown(sender);
                    return false;
                }

                subCommand.execute(sender, args);
                return true;
            }
        } else {
            this.showHelp(sender);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabs = new ArrayList();

        if (args.length == 1) {

            for (SubCommand subCommand : getAllowedCommands(sender)) {
                tabs.add(subCommand.getCommand());
            }

        } else {

            for (SubCommand allowedCommand : getAllowedCommands(sender)) {
                allowedCommand.getTabCompletions(tabs, sender, args);
            }

        }

        return tabs;
    }

    private SubCommand getSubCommand(String arg) {
        return this.subCommands.stream().filter((subCommand) -> subCommand.getCommand().equalsIgnoreCase(arg)).findFirst().orElse(null);
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(CC.CHAT_LINE);

        sender.sendMessage(ChatColor.AQUA + "ActriumGraves Available Commands:");
        sender.sendMessage(ChatColor.RESET + " ");

        List<SubCommand> commands = this.getAllowedCommands(sender);
        if (commands.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "You do not have access to any commands.");
        } else {
            for (SubCommand subCommand : commands) {
                sender.sendMessage(ChatColor.AQUA + "/grave " + subCommand.getCommand() + ChatColor.GRAY + " - " + ChatColor.RESET + subCommand.getDescription());
            }
        }

        sender.sendMessage(CC.CHAT_LINE);
    }

    private List<SubCommand> getAllowedCommands(CommandSender sender) {
        List<SubCommand> commands = new ArrayList<>();


        for (SubCommand subCommand : subCommands) {
            if (!(sender instanceof Player) && subCommand.isPlayerOnly()) {
                continue;
            }

            if (sender.hasPermission(subCommand.getPermission())) {
                commands.add(subCommand);
            }

        }
        return commands;
    }

    @SuppressWarnings({"DataFlowIssue"})
    public static void sendUnknown(CommandSender sender) {
        sender.sendMessage(Bukkit.spigot().getConfig().getString("unknown-command"));
    }

    public static Death getGraveLookingAt(Player player) {
        Block block = player.getTargetBlockExact(4);
        if (block == null) {
            return null;
        }

        return GraveManager.get().getGrave(block.getLocation());

    }
}
