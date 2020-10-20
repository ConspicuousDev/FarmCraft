package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.Stat;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtinjector.NBTInjector;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityListener implements Listener {
    public static List<ArmorStand> damageDisplays = new ArrayList<>();

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            e.setCancelled(true);
            Player p = (Player) e.getDamager();
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            Entity entity = NBTInjector.patchEntity(e.getEntity());
            NBTCompound compound = NBTInjector.getNbtData(entity);
            if (compound.hasKey("ID")) {
                if (compound.getInteger("HP") > 0) {
                    Location location = e.getEntity().getLocation();
                    int damage = (int) (Math.floor(5 + 0 + Math.floor(user.getStat(Stat.STRENGTH) / 5)) * (1 + Math.floor(user.getStat(Stat.STRENGTH) / 100)));
                    String damageString = "&7" + damage;
                    if (Math.random() < (double) user.getStat(Stat.CRITICAL_CHANCE) / 100) {
                        damage = (int) (damage * ((float) user.getStat(Stat.CRITICAL_DAMAGE) / 100));
                        damageString = "&f✧";
                        for (int i = 0; i < String.valueOf(damage).length(); i++) {
                            if (i == 0) {
                                damageString += "&f";
                            } else if (i == 1) {
                                damageString += "&e";
                            } else if (i == 2) {
                                damageString += "&6";
                            } else {
                                damageString += "&c";
                            }
                            damageString += String.valueOf(damage).charAt(i);
                        }
                        damageString += "✧";
                    }
                    ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(location.add(Math.random() - 0.5, ((LivingEntity) e.getEntity()).getEyeHeight() + 0.5, Math.random() - 0.5), EntityType.ARMOR_STAND);
                    armorStand.setMarker(true);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCanPickupItems(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(Methods.color(damageString));
                    damageDisplays.add(armorStand);
                    String color = "&a";
                    if (compound.getInteger("HP") - damage < 1) {
                        color = "&e";
                        compound.setInteger("HP", 0);
                        entity.playEffect(EntityEffect.DEATH);
                        int i = Bukkit.getScheduler().scheduleSyncDelayedTask(FarmCraft.instance, new Runnable() {
                            @Override
                            public void run() {
                                entity.remove();
                            }
                        }, 20);
                    } else {
                        if (compound.getInteger("HP") - damage < compound.getInteger("MAX_HP") / 2) {
                            color = "&e";
                        }
                        entity.playEffect(EntityEffect.HURT);
                        compound.setInteger("HP", compound.getInteger("HP") - damage);
                    }
                    entity.setCustomName(Methods.color("&7[Nvl " + compound.getInteger("LEVEL") + "] &c" + entity.getType() + " " + color + compound.getInteger("HP") + "&f/&a" + compound.getInteger("MAX_HP") + " " + Stat.HEALTH.getColor() + Stat.HEALTH.getSymbol()));
                    int i = Bukkit.getScheduler().scheduleSyncDelayedTask(FarmCraft.instance, new Runnable() {
                        @Override
                        public void run() {
                            armorStand.remove();
                            damageDisplays.remove(armorStand);
                        }
                    }, 20);
                } else {
                    entity.remove();
                }
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e.getEntity().getType() != EntityType.ARMOR_STAND && e.getEntity().getType() != EntityType.PLAYER && e.getEntity().getType() != EntityType.DROPPED_ITEM) {
            int hp = 100;
            int level = 10;
            Entity entity = NBTInjector.patchEntity(e.getEntity());
            entity.setCustomNameVisible(true);
            entity.setCustomName(Methods.color("&7[Nvl " + level + "] &c" + entity.getType() + " &a" + hp + "&f/&a" + hp + " " + Stat.HEALTH.getColor() + Stat.HEALTH.getSymbol()));
            NBTCompound compound = NBTInjector.getNbtData(entity);
            compound.setInteger("HP", hp);
            compound.setInteger("MAX_HP", hp);
            compound.setInteger("LEVEL", level);
            compound.setString("ID", entity.getType().toString());
        }
    }
}
