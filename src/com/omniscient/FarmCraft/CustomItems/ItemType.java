package com.omniscient.FarmCraft.CustomItems;

public enum ItemType {
    WEAPON("ARMA"),
    ARMOR("ARMADURA"),
    RESOURCE("RECURSO"),
    ROD("VARA DE PESCA"),
    TOOL("FERRAMENTA"),
    ARTIFACT("ARTEFATO"),
    NONE("");

    String ID;
    String name;

    ItemType(String name) {
        this.ID = this.name();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
