package com.omniscient.FarmCraft.CustomItems.Abilities;

import com.omniscient.FarmCraft.CustomItems.Ability;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class NotPlaceableAbility implements Ability {
    String name = "Incolocável";
    String description = "&7Torna impossível colocar esse bloco em qualquer lugar do mundo!";
    String input = "CLIQUE DIREITO";
    boolean hidden = true;
    double cooldown = 0;

    @Override
    public boolean meetsCondition(Event event) {
        return event instanceof BlockPlaceEvent;
    }

    @Override
    public void execute(Event event) {
        BlockPlaceEvent e = (BlockPlaceEvent) event;
        e.setCancelled(true);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getInput() {
        return this.input;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public double getCooldown() {
        return this.cooldown;
    }
}
