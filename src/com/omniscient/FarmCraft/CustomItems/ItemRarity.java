package com.omniscient.FarmCraft.CustomItems;

import org.bukkit.ChatColor;

public enum ItemRarity {
    COMMON(ChatColor.WHITE, "COMUM"),
    UNCOMMON(ChatColor.GREEN, "INCOMUM"),
    RARE(ChatColor.BLUE, "RARO"),
    EPIC(ChatColor.DARK_PURPLE, "ÉPICO"),
    LEGENDARY(ChatColor.GOLD, "LENDÁRIO"),
    MYTHIC(ChatColor.LIGHT_PURPLE, "MÍTICO"),
    SPECIAL(ChatColor.RED, "ESPECIAL");

    ChatColor color;
    String name;

    ItemRarity(ChatColor color, String name) {
        this.color = color;
        this.name = name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }
}
