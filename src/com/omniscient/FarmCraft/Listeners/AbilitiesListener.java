package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.CustomItems.Ability;
import com.omniscient.FarmCraft.CustomItems.CustomItem;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.simple.parser.ParseException;

public class AbilitiesListener implements Listener {
    @EventHandler
    public void onInteractAbility(PlayerInteractEvent e) throws ParseException {
        Player p = e.getPlayer();
        callAbility(p, e);
    }

    @EventHandler
    public void onFishAbility(PlayerFishEvent e) throws ParseException {
        Player p = e.getPlayer();
        callAbility(p, e);
    }

    @EventHandler
    public void onBlockPlaceAbility(BlockPlaceEvent e) throws ParseException {
        Player p = e.getPlayer();
        callAbility(p, e);
    }

    @EventHandler
    public void onPlayerTakeDamageAbility(EntityDamageEvent e) throws ParseException {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            callAbility(p, e);
        }
    }

    public void callAbility(Player p, Event e) throws ParseException {
        if (p.getItemInHand().getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(p.getItemInHand());
            if (nbtItem.hasNBTData() && nbtItem.getKeys().contains("ID")) {
                String id = nbtItem.getString("ID");
                CustomItem customItem = CustomItem.valueOf(id);
                for (Ability ability : customItem.getAbilities()) {
                    ability.run(e, p, ability);
                }
            }
        }
    }
}
