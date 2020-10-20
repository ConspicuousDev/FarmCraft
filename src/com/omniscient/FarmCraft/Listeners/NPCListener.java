package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Methods;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class NPCListener implements Listener {
    public static HashMap<String, EntityPlayer> npcList = new HashMap<>();
    public static HashMap<EntityPlayer, Integer> npcIdList = new HashMap<>();
    public HashMap<String, EntityArmorStand> armorStandList = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) throws SQLException {
        Player p = e.getPlayer();
        ResultSet npcSet = Methods.getNPCs();
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        while (npcSet.next()) {
            EntityPlayer npc;
            Location npcLocation = new Location(Bukkit.getWorld(npcSet.getString("world")), npcSet.getInt("x"), npcSet.getInt("y"), npcSet.getInt("z"));
            if (p.getLocation().getWorld().getName().equals(npcSet.getString("world"))) {
                if (p.getLocation().distance(npcLocation) <= 15) {
                    if (!npcList.containsKey(p.getDisplayName() + "-" + npcSet.getInt("id"))) {
                        if (npcSet.getString("action_text") == null) {
                            npc = Methods.createNPC(npcSet.getString("name"), npcLocation.getWorld().getName(), npcLocation, npcSet.getInt("head_rotation"), npcSet.getString("texture"), npcSet.getString("signature"));
                        } else {
                            npc = Methods.createNPC(npcSet.getString("action_text"), npcLocation.getWorld().getName(), npcLocation, npcSet.getInt("head_rotation"), npcSet.getString("texture"), npcSet.getString("signature"));
                            EntityArmorStand armorStand = Methods.createArmorStand(npcSet.getString("name"), npcLocation.getWorld().getName(), npcLocation);
                            connection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
                            armorStandList.put(p.getDisplayName() + "-" + npcSet.getInt("id"), armorStand);
                        }
                        npcList.put(p.getDisplayName() + "-" + npcSet.getInt("id"), npc);
                        npcIdList.put(npc, npcSet.getInt("id"));
                        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(FarmCraft.instance, new Runnable() {
                            @Override
                            public void run() {
                                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                            }
                        }, 2);
                    } else if (p.getLocation().distance(npcLocation) < 10) {
                        npc = npcList.get(p.getDisplayName() + "-" + npcSet.getInt("id"));
                        npcLocation = npcLocation.setDirection(p.getLocation().subtract(npcLocation).toVector());
                        float yaw = npcLocation.getYaw();
                        float pitch = npcLocation.getPitch();
                        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false));
                        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) ((yaw % 360.) * 256 / 360)));
                    } else {
                        npc = npcList.get(p.getDisplayName() + "-" + npcSet.getInt("id"));
                        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) (128 + npcSet.getInt("head_rotation") * 64), (byte) 0, false));
                        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (128 + npcSet.getInt("head_rotation") * 64)));
                    }
                } else {
                    if (npcList.containsKey(p.getDisplayName() + "-" + npcSet.getInt("id"))) {
                        npc = npcList.get(p.getDisplayName() + "-" + npcSet.getInt("id"));
                        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
                        npcList.remove(p.getDisplayName() + "-" + npcSet.getInt("id"));
                        npcIdList.remove(npc);
                        if (armorStandList.containsKey(p.getDisplayName() + "-" + npcSet.getInt("id"))) {
                            EntityArmorStand armorStand = armorStandList.get(p.getDisplayName() + "-" + npcSet.getInt("id"));
                            connection.sendPacket(new PacketPlayOutEntityDestroy(armorStand.getId()));
                            armorStandList.remove(p.getDisplayName() + "-" + npcSet.getInt("id"));
                        }
                    }
                }
            }
        }
    }
}
