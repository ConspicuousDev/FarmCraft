package com.omniscient.FarmCraft.CustomItems.Abilities;

import com.omniscient.FarmCraft.CustomItems.Ability;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SayNBTAbility implements Ability {
    int depth;
    String name = "NBT";
    String description = "&7Diz quais são as Tags NBT do item.";
    String input = "CLIQUE DIREITO";
    boolean hidden = true;
    double cooldown = 0;

    @Override
    public boolean meetsCondition(Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent e = (PlayerInteractEvent) event;
            return e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR;
        }
        return false;
    }

    @Override
    public void execute(Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent e = (PlayerInteractEvent) event;
            NBTItem nbtItem = new NBTItem(e.getItem());
            for (String key : nbtItem.getKeys()) {
                e.getPlayer().sendMessage(Methods.color(key + " : " + nbtItem.getString(key)));
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description.replace("'depth'", String.valueOf(depth));
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

    public int getDepth() {
        return this.depth;
    }
}
