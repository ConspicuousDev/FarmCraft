package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.Utils.Methods;

public enum Stat {
    HEALTH("&c", "Vida", "❤"),
    DEFENSE("&a", "Defesa", "❈"),
    CRITICAL_DAMAGE("&9", "Dano Crítico", "☠"),
    CRITICAL_CHANCE("&9", "Chance Crítica", "☣"),
    STRENGTH("&c", "Força", "✶"),
    INTELLIGENCE("&b", "Inteligência", "✎"),
    SPEED("&f", "Velocidade", "✦"),
    LUCK("&d", "Sorte", "₪"),
    REGEN("&5", "Regeneração", "❤/s");

    String color;
    String name;
    String symbol;

    Stat(String color, String name, String symbol) {
        this.color = color;
        this.name = Methods.color(color + name);
        this.symbol = Methods.color(color + symbol);
    }

    public String getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
