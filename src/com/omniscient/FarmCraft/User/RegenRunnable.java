package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.FarmCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RegenRunnable {
    public RegenRunnable(User user) {
        Player p = user.getPlayer();
        user.setRegenTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(FarmCraft.instance, new Runnable() {
            @Override
            public void run() {
                user.setStat(Stat.HEALTH, user.getStat(Stat.HEALTH) + user.getStat(Stat.REGEN));
            }
        }, 0, 20));
    }
}
