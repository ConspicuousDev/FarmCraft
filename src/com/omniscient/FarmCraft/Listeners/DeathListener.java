package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        e.setDeathMessage("");
        user.kill();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        if (p.getLocation().getY() < -2) {
            user.kill();
        }
    }
}
