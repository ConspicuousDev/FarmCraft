package com.omniscient.FarmCraft.CustomItems;

public enum Enchantment {
    SHARPNESS("Afiação", 5, "&7Aumenta o dano causado em &a10%&7.");

    String ID;
    String name;
    int maxLevel;
    String description;
    int level;

    Enchantment(String name, int maxLevel, String description) {
        this.ID = name();
        this.name = name;
        this.maxLevel = maxLevel;
        this.description = description;
        this.level = 0;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        if (level > maxLevel) {
            level = maxLevel;
        }
        this.level = level;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
