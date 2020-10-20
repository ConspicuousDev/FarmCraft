package com.omniscient.FarmCraft.Terrain;

import com.omniscient.FarmCraft.Utils.Methods;

public enum TerrainType {
    PLAINS("&aPlanície", 200, 0.5F, 0.5F, 8, 56),
    LARGE_MOUNTAINS("&bMontanhas Altas", 100, 0F, 0.4F, 60, 56),
    NONE("&7Nenhum", 1, 0.5F, 0F, 1, 0);

    String ID;
    String name;
    int generalScale;
    float frequency;
    float amplitude;
    int yScale;
    int minHeight;

    TerrainType(String name, int generalScale, float frequency, float amplitude, int yScale, int minHeight) {
        this.ID = this.name();
        this.name = Methods.color(name);
        this.generalScale = generalScale;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.yScale = yScale;
        this.minHeight = minHeight;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getGeneralScale() {
        return this.generalScale;
    }

    public float getFrequency() {
        return this.frequency;
    }

    public float getAmplitude() {
        return this.amplitude;
    }

    public int getYScale() {
        return this.yScale;
    }

    public int getMinHeight() {
        return this.minHeight;
    }
}
