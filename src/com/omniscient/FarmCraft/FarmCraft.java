package com.omniscient.FarmCraft;

import com.omniscient.FarmCraft.Utils.Database;
import com.omniscient.FarmCraft.Utils.Definitions;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmCraft extends JavaPlugin {
    public static FarmCraft instance;
    public static String PREFIX = "&e[&fFarm&bCraft&e]";

    public void onEnable() {
        instance = this;
        Database.connect();
        Definitions.listeners();
        Definitions.commands();
        Methods.consoleLog("&aPlugin inicializado.");
    }

    public void onDisable() {
        Methods.consoleLog("&cPlugin desabilitado.");
    }
}
