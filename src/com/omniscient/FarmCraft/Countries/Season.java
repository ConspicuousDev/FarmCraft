package com.omniscient.FarmCraft.Countries;

import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.block.Biome;

public enum Season {
    EARLY_SUMMER("&eVerão", "&eInício do Verão", Biome.PLAINS),
    SUMMER("&eVerão", "&eVerão", Biome.PLAINS),
    LATE_SUMMER("&eVerão", "&eFim do Verão", Biome.PLAINS),
    EARLY_AUTUMN("&6Outono", "&6Início do Outono", Biome.SAVANNA),
    AUTUMN("&6Outono", "&6Outono", Biome.SAVANNA),
    LATE_AUTUMN("&6Outono", "&6Fim do Outono", Biome.SAVANNA),
    EARLY_WINTER("&fInverno", "&fInício do Inverno", Biome.TAIGA),
    WINTER("&fInverno", "&fInverno", Biome.TAIGA),
    LATE_WINTER("&fInverno", "&fFim do Inverno", Biome.TAIGA),
    EARLY_SPRING("&dPrimavera", "&dInício da Primavera", Biome.JUNGLE),
    SPRING("&dPrimavera", "&dPrimavera", Biome.JUNGLE),
    LATE_SPRING("&dPrimavera", "&dFim da Primavera", Biome.JUNGLE);

    String name;
    String fullName;
    Biome biome;

    Season(String name, String fullName, Biome biome) {
        this.name = Methods.color(name);
        this.fullName = Methods.color(fullName);
        this.biome = biome;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Biome getBiome() {
        return this.biome;
    }
}
