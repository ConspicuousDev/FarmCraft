package com.omniscient.FarmCraft.Terrain;

public class ResourceNode {
    Resource resource;
    int cap;

    public ResourceNode(Resource resource, int cap) {
        this.resource = resource;
        this.cap = cap;
    }

    public Resource getResource() {
        return this.resource;
    }

    public int getCap() {
        return cap;
    }
}
