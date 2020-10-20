package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.Countries.MapLocation;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.User.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MapLocationListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        Location playerLocation = p.getLocation();
        Country country = user.getCountry();
        HashMap<Integer, MapLocation> possibleLocationsArea = new HashMap<>();
        if (user.getCompany() != null) {
            for (Terrain terrain : user.getCompany().getTerrains()) {
                if (p.getWorld().getName().equals(terrain.getID())) {
                    user.setLocation(MapLocation.YOUR_TERRAIN);
                    return;
                }
            }
        }
        for (MapLocation mapLocation : getCountryMapLocations(country)) {
            int minX = Math.min(mapLocation.getLocation1().getBlockX(), mapLocation.getLocation2().getBlockX());
            int maxX = Math.max(mapLocation.getLocation1().getBlockX(), mapLocation.getLocation2().getBlockX());
            int x = playerLocation.getBlockX();
            int minY = Math.min(mapLocation.getLocation1().getBlockY(), mapLocation.getLocation2().getBlockY());
            int maxY = Math.max(mapLocation.getLocation1().getBlockY(), mapLocation.getLocation2().getBlockY());
            int y = playerLocation.getBlockY();
            int minZ = Math.min(mapLocation.getLocation1().getBlockZ(), mapLocation.getLocation2().getBlockZ());
            int maxZ = Math.max(mapLocation.getLocation1().getBlockZ(), mapLocation.getLocation2().getBlockZ());
            int z = playerLocation.getBlockZ();
            if (minX <= x && x <= maxX) {
                if (minY <= y && y <= maxY) {
                    if (minZ <= z && z <= maxZ) {
                        possibleLocationsArea.put(mapLocation.getArea(), mapLocation);
                    }
                }
            }
        }
        if (possibleLocationsArea.keySet().size() > 0) {
            List<Integer> mapLocationVolumes = new ArrayList<>(possibleLocationsArea.keySet());
            Collections.sort(mapLocationVolumes);
            MapLocation mapLocation = possibleLocationsArea.get(mapLocationVolumes.get(0));
            user.setLocation(mapLocation);
        } else {
            user.setLocation(MapLocation.NONE);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        Location playerLocation = p.getLocation();
        Country country = user.getCountry();
        HashMap<Integer, MapLocation> possibleLocationsArea = new HashMap<>();
        if (user.getCompany() != null) {
            for (Terrain terrain : user.getCompany().getTerrains()) {
                if (p.getWorld().getName().equals(terrain.getID())) {
                    user.setLocation(MapLocation.YOUR_TERRAIN);
                    return;
                }
            }
        }
        for (MapLocation mapLocation : getCountryMapLocations(country)) {
            int minX = Math.min(mapLocation.getLocation1().getBlockX(), mapLocation.getLocation2().getBlockX());
            int maxX = Math.max(mapLocation.getLocation1().getBlockX(), mapLocation.getLocation2().getBlockX());
            int x = playerLocation.getBlockX();
            int minY = Math.min(mapLocation.getLocation1().getBlockY(), mapLocation.getLocation2().getBlockY());
            int maxY = Math.max(mapLocation.getLocation1().getBlockY(), mapLocation.getLocation2().getBlockY());
            int y = playerLocation.getBlockY();
            int minZ = Math.min(mapLocation.getLocation1().getBlockZ(), mapLocation.getLocation2().getBlockZ());
            int maxZ = Math.max(mapLocation.getLocation1().getBlockZ(), mapLocation.getLocation2().getBlockZ());
            int z = playerLocation.getBlockZ();
            if (minX <= x && x <= maxX) {
                if (minY <= y && y <= maxY) {
                    if (minZ <= z && z <= maxZ) {
                        possibleLocationsArea.put(mapLocation.getArea(), mapLocation);
                    }
                }
            }
        }
        if (possibleLocationsArea.keySet().size() > 0) {
            List<Integer> mapLocationVolumes = new ArrayList<>(possibleLocationsArea.keySet());
            Collections.sort(mapLocationVolumes);
            MapLocation mapLocation = possibleLocationsArea.get(mapLocationVolumes.get(0));
            user.setLocation(mapLocation);
        } else {
            user.setLocation(MapLocation.NONE);
        }
    }

    public static List<MapLocation> getCountryMapLocations(Country country) {
        List<MapLocation> mapLocations = new ArrayList<>();
        for (MapLocation mapLocation : MapLocation.values()) {
            if (mapLocation.getCountry().equals(country)) {
                mapLocations.add(mapLocation);
            }
        }
        return mapLocations;
    }
}
