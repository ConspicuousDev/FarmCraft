package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StatBarRunnable {
    public StatBarRunnable(User user) {
        Player p = user.getPlayer();
        user.setStatBarTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(FarmCraft.instance, new Runnable() {
            @Override
            public void run() {
                String statBar = "";
                for (Stat stat : Stat.values()) {
                    statBar += stat.getColor() + user.getStat(stat) + "/" + user.getMaxStat(stat) + stat.getSymbol() + " ";
                }
                Methods.sendActionBar(p, Methods.color(statBar));
            }
        }, 0, 20));
    }
}
