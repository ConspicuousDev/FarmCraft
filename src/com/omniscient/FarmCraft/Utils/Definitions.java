package com.omniscient.FarmCraft.Utils;

import com.omniscient.FarmCraft.Commands.FarmCraftCommand;
import org.bukkit.Bukkit;

public class Definitions {
    public static void commands() {
        Bukkit.getPluginCommand("farmcraft").setExecutor(new FarmCraftCommand());
    }

    public static void listeners() {
        //Bukkit.getPluginManager().registerEvents(new BlockListener(), FarmCraft.instance);
    }
}
