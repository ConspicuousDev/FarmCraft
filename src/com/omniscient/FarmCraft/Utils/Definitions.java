package com.omniscient.FarmCraft.Utils;

import com.omniscient.FarmCraft.Block.BlockListener;
import com.omniscient.FarmCraft.Commands.CommandListener;
import com.omniscient.FarmCraft.Commands.FarmCraftCommand;
import com.omniscient.FarmCraft.Commands.RankCommand;
import com.omniscient.FarmCraft.Commands.StatsCommand;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Listeners.*;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public class Definitions {
    public static void listeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinQuitListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new DefaultStatsListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new PortalListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerPingListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new FarmCraftMenuListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new AbilitiesListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemUpdateListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemCraftListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new PotionListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new ScoreboardListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new TimeListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new MapLocationListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantmentListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new DeathListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new PotionListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityListener(), FarmCraft.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new CommandListener(), FarmCraft.instance);
    }

    public static void commands() {
        Bukkit.getPluginCommand("farmcraft").setExecutor(new FarmCraftCommand());
        Bukkit.getPluginCommand("rank").setExecutor(new RankCommand());
        Bukkit.getPluginCommand("stats").setExecutor(new StatsCommand());
    }

    public static void dependencies() {
        List<String> dependencies = Arrays.asList("WorldEdit");
        for (String dependency : dependencies) {
            if (Bukkit.getPluginManager().getPlugin(dependency) == null) {
                Methods.consoleLog("&4ERRO &c- O plugin não encontrou a seguinte dependência: &7" + dependency + "&c!");
            }
        }
    }
}
