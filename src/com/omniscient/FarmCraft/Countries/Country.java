package com.omniscient.FarmCraft.Countries;

import com.google.common.collect.ImmutableMap;
import com.omniscient.FarmCraft.Listeners.TimeListener;
import com.omniscient.FarmCraft.Terrain.Resource;
import com.omniscient.FarmCraft.Terrain.TerrainType;
import com.omniscient.FarmCraft.Utils.Methods;

import java.util.*;

public enum Country {
    BRAZIL(Methods.color("&aBrasil"), -3, Hemisphere.SOUTH, ImmutableMap.of(TerrainType.PLAINS, Arrays.asList(Resource.STONE, Resource.COAL, Resource.IRON))),
    ICELAND(Methods.color("&bIslândia"), 0, Hemisphere.NORTH, ImmutableMap.of(TerrainType.LARGE_MOUNTAINS, Arrays.asList(Resource.STONE))),
    NONE(Methods.color("&7Nenhum"), 0, Hemisphere.NONE, null);

    String ID;
    String name;
    int timeOffset;
    Hemisphere hemisphere;
    Map<TerrainType, List<Resource>> terrainResources;

    Country(String name, int timeOffset, Hemisphere hemisphere, Map<TerrainType, List<Resource>> terrainResources) {
        this.ID = this.name();
        this.name = name;
        this.timeOffset = timeOffset;
        this.hemisphere = hemisphere;
        this.terrainResources = terrainResources;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getTimeOffset() {
        return this.timeOffset;
    }

    public Hemisphere getHemisphere() {
        return this.hemisphere;
    }

    public Map<TerrainType, List<Resource>> getTerrainResources() {
        return this.terrainResources;
    }

    public TerrainType getRandomTerrainType() {
        List<TerrainType> terrainTypes = new ArrayList<>(this.terrainResources.keySet());
        return terrainTypes.get(new Random().nextInt(terrainTypes.size()));
    }

    public int getHour(int hour) {
        if (hour + this.timeOffset < 0) {
            return 24 + (hour + this.timeOffset);
        } else if (hour + this.timeOffset > 24) {
            return (hour + this.timeOffset) - 24;
        } else {
            return hour + this.timeOffset;
        }
    }

    public int getDay(int hour, int day, Month month) {
        if (hour + this.timeOffset < 0) {
            if (day - 1 < 1) {
                Month m = TimeListener.getLastMonth(month.getID());
                return m.getDays();
            }
            return day - 1;
        } else if (hour + this.timeOffset > 24) {
            if (day + 1 > month.getDays()) {
                return 1;
            }
            return day + 1;
        } else {
            return day;
        }
    }

    public Month getMonth(int hour, int day, Month month) {
        if (hour + this.timeOffset < 0) {
            if (day - 1 < 1) {
                return TimeListener.getLastMonth(month.getID());
            }
            return TimeListener.month;
        } else if (hour + this.timeOffset > 24) {
            if (day + 1 > TimeListener.month.getDays()) {
                return TimeListener.getNextMonth(month.getID());
            }
            return TimeListener.month;
        } else {
            return TimeListener.month;
        }
    }

    public Season getSeason(Month month) {
        int monthID = month.getID();
        List<Season> seasons = this.hemisphere.getSeasons();
        return seasons.get(monthID - 1);
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    public void setHemisphere(Hemisphere hemisphere) {
        this.hemisphere = hemisphere;
    }
}
