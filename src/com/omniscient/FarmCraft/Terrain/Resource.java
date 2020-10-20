package com.omniscient.FarmCraft.Terrain;

import com.omniscient.FarmCraft.CustomItems.CustomItem;

public enum Resource {
    IRON(CustomItem.STONE_RESOURCE, 16, 100, 300),
    COAL(CustomItem.STONE_RESOURCE, 40, 200, 500),
    STONE(CustomItem.STONE_RESOURCE, 200, 500, 1000);

    String ID;
    CustomItem customItem;
    String name;
    int weight;
    int minNode;
    int maxNode;

    Resource(CustomItem customItem, int weight, int minNode, int maxNode) {
        this.ID = this.name();
        this.customItem = customItem;
        this.name = customItem.getName();
        this.weight = weight;
        this.minNode = minNode;
        this.maxNode = maxNode;
    }

    public String getID() {
        return this.ID;
    }

    public CustomItem getCustomItem() {
        return this.customItem;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getMinNode() {
        return this.minNode;
    }

    public int getMaxNode() {
        return this.maxNode;
    }
}
