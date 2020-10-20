package com.omniscient.FarmCraft.CustomItems.Abilities;

import com.omniscient.FarmCraft.CustomItems.Ability;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GrappleAbility implements Ability {
    public static List<Player> grapplePlayers = new ArrayList<>();
    String name = "Agarrar";
    String description = "&7Impulsiona o jogador na direção do gancho, permitindo uma melhor movimentação pelo mapa.";
    String input = "CLIQUE DIREITO";
    boolean hidden = false;
    double cooldown = 2;

    @Override
    public boolean meetsCondition(Event event) {
        if (event instanceof PlayerFishEvent) {
            PlayerFishEvent e = (PlayerFishEvent) event;
            Player p = e.getPlayer();
            if (!grapplePlayers.contains(p)) {
                grapplePlayers.add(p);
            }
            return e.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT);
        }
        return false;
    }

    @Override
    public void execute(Event event) {
        if (event instanceof PlayerFishEvent) {
            PlayerFishEvent e = (PlayerFishEvent) event;
            Player p = e.getPlayer();
            Vector baseVector = e.getHook().getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
            p.setVelocity(baseVector.multiply(5).setY(1));
        }
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
