package com.omniscient.FarmCraft.Countries;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Hemisphere {
    SOUTH(Arrays.asList(Season.EARLY_SUMMER, Season.SUMMER, Season.LATE_SUMMER, Season.EARLY_AUTUMN, Season.AUTUMN, Season.LATE_AUTUMN, Season.EARLY_WINTER, Season.WINTER, Season.LATE_WINTER, Season.EARLY_SPRING, Season.SPRING, Season.LATE_SPRING)),
    NORTH(Arrays.asList(Season.EARLY_WINTER, Season.WINTER, Season.LATE_WINTER, Season.EARLY_SPRING, Season.SPRING, Season.LATE_SPRING, Season.EARLY_SUMMER, Season.SUMMER, Season.LATE_SUMMER, Season.EARLY_AUTUMN, Season.AUTUMN, Season.LATE_AUTUMN)),
    NONE(Collections.emptyList());

    List<Season> seasons;

    Hemisphere(List<Season> seasons) {
        this.seasons = Collections.emptyList();
        if (seasons.size() == 12) {
            this.seasons = seasons;
        }
    }

    public List<Season> getSeasons() {
        return this.seasons;
    }
}
