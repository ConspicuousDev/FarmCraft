package com.omniscient.FarmCraft.Countries;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MapLocation {
    VILLAGE_BRAZIL("&bVila", Country.BRAZIL, -119, 50, -202, 41, 100, 20, Arrays.asList(Material.STAINED_CLAY)),
    AUCTION_HOUSE_BRAZIL("&6Leiloeiro", Country.BRAZIL, -19, 60, -102, -59, 89, -80, Arrays.asList()),
    YOUR_TERRAIN("&aSua Terra", Country.NONE, 0, 0, 0, 0, 0, 0, Collections.emptyList()),
    NONE("&7Nenhum", Country.NONE, 0, 0, 0, 0, 0, 0, Collections.emptyList());

    String ID;
    String name;
    Country country;
    Location location1;
    Location location2;
    int area;
    List<Material> allowedBlocks;

    MapLocation(String name, Country country, int x1, int y1, int z1, int x2, int y2, int z2, List<Material> allowedBlocks) {
        this.ID = this.name();
        this.name = name;
        this.country = country;
        this.location1 = new Location(Bukkit.getWorld(country.getID()), x1, y1, z1);
        this.location2 = new Location(Bukkit.getWorld(country.getID()), x2, y2, z2);
        this.area = Math.abs(Math.max(x1, x2) - Math.min(x1, x2)) */*Math.abs(Math.max(y1, y2)-Math.min(y1, y2))**/Math.abs(Math.max(z1, z2) - Math.min(z1, z2));
        this.allowedBlocks = allowedBlocks;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public Country getCountry() {
        return this.country;
    }

    public Location getLocation1() {
        return this.location1;
    }

    public Location getLocation2() {
        return this.location2;
    }

    public int getArea() {
        return this.area;
    }

    public List<Material> getAllowedBlocks() {
        return this.allowedBlocks;
    }
}
