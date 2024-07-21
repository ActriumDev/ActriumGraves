package xyz.actrium.graves.util;

import org.bukkit.ChatColor;

public class CC {
    public static String PREFIX = "§8[§bActriumGraves§8] §r";
    public static String CHAT_LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------------------";

    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
