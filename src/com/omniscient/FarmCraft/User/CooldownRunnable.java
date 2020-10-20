package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.CustomItems.Ability;
import com.omniscient.FarmCraft.FarmCraft;
import org.bukkit.Bukkit;

public class CooldownRunnable {
    public CooldownRunnable(User user, Ability ability) {
        int i = Bukkit.getScheduler().scheduleSyncDelayedTask(FarmCraft.instance, new Runnable() {
            @Override
            public void run() {
                user.cooldownList.remove(ability);
            }
        }, (long) (20 * ability.getCooldown()));
    }
}
