package com.omniscient.FarmCraft.Utils;

import com.omniscient.FarmCraft.FarmCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Methods {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void consoleLog(String string) {
        Bukkit.getConsoleSender().sendMessage(color(FarmCraft.PREFIX + " &f" + string));
    }

    public static void kick() {

    }
}
