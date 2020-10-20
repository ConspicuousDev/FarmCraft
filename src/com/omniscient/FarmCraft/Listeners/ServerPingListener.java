package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener {
    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        e.setMotd(Methods.color(FarmCraft.MOTD));
    }
}
