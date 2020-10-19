package com.omniscient.FarmCraft;

import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmCraft extends JavaPlugin {
    public static String PREFIX = "&e[&fFarmCraft&e]";

    public void onEnable(){
        Methods.consoleLog("&aPlugin inicializado.");
    }
    public void onDisable(){
        Methods.consoleLog("&cPlugin desabilitado.");
    }
}
