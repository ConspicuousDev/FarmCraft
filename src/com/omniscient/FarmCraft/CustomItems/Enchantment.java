package com.omniscient.FarmCraft.CustomItems;

public enum Enchantment {
    SHARPNESS("Afiação", 5, "&7Aumenta o dano causado em &a10%&7.");

    String ID;
    String name;
    int maxLevel;
    String description;

    Enchantment(String name, int maxLevel, String description) {
        this.ID = name();
        this.name = name;
        this.maxLevel = maxLevel;
        this.description = description;
    }

    public String getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public int getMaxLevel() {
        return maxLevel;
    }
    public String getDescription() {
        return description;
    }
}
