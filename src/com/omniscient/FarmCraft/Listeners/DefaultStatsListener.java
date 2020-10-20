package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.Stat;
import com.omniscient.FarmCraft.User.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class DefaultStatsListener implements Listener {
    @EventHandler
    public void onHugerChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            p.setFoodLevel(20);
            p.setSaturation(20);
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
            Player p = (Player) e.getEntity();
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                int health = (int) (user.getMaxStat(Stat.HEALTH) * e.getDamage() / p.getMaxHealth());
                user.setStat(Stat.HEALTH, user.getStat(Stat.HEALTH) - health);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.POISON || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                if (p.getWorld().getBlockAt(p.getLocation()).getType() == Material.FIRE || p.getWorld().getBlockAt(p.getLocation()).getType() == Material.LAVA || p.getWorld().getBlockAt(p.getLocation()).getType() == Material.STATIONARY_LAVA) {
                    user.setStat(Stat.HEALTH, user.getStat(Stat.HEALTH) - (user.getStat(Stat.HEALTH) / 10 + 5));
                } else {
                    user.setStat(Stat.HEALTH, user.getStat(Stat.HEALTH) - 10);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRegen(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
            ((Player) e.getEntity()).setFoodLevel(20);
            ((Player) e.getEntity()).setSaturation(20);
        }
    }
}
