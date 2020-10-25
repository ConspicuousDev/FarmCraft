package com.omniscient.FarmCraft.Commands;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        User u = FarmCraft.onlineUsers.get(p.getDisplayName());
        String command = e.getMessage().replace("/", "");
        if (command.startsWith("reload")) {
            p.sendMessage(Methods.color("&cEste commando está bloqueado. Digite &7/restart &ccaso queira reiniciar o servidor."));
            e.setCancelled(true);
        }
    }
}
