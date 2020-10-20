package com.omniscient.FarmCraft.Block;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockListener implements Listener {
    public static List<ReplaceBlockRunnable> blockReplaceRunnables = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();
        Player p = e.getPlayer();
        List<Material> relatives = Arrays.asList(b.getRelative(BlockFace.DOWN).getType(), b.getRelative(BlockFace.UP).getType(), b.getRelative(BlockFace.NORTH).getType(), b.getRelative(BlockFace.EAST).getType(), b.getRelative(BlockFace.SOUTH).getType(), b.getRelative(BlockFace.WEST).getType());
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        if (user.getCompany() != null) {
            for (Terrain terrain : user.getCompany().getTerrains()) {
                if (terrain.getID().equals(p.getWorld().getName())) {
                    if (relatives.contains(Material.PORTAL)) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Methods.color("&cVocê não pode alterar o bloco ao lado de um portal! Remova o portal antes de fazê-lo."));
                    }
                    return;
                }
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();
        List<Material> relatives = Arrays.asList(b.getRelative(BlockFace.DOWN).getType(), b.getRelative(BlockFace.UP).getType(), b.getRelative(BlockFace.NORTH).getType(), b.getRelative(BlockFace.EAST).getType(), b.getRelative(BlockFace.SOUTH).getType(), b.getRelative(BlockFace.WEST).getType());
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        if (user.getCompany() != null) {
            for (Terrain terrain : user.getCompany().getTerrains()) {
                if (terrain.getID().equals(p.getWorld().getName())) {
                    if (relatives.contains(Material.PORTAL)) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Methods.color("&cVocê não pode alterar o bloco ao lado de um portal! Remova o portal antes de fazê-lo."));
                    }
                    return;
                }
            }
        }
        if (!user.getLocation().getAllowedBlocks().contains(e.getBlock().getType())) {
            e.setCancelled(true);
        } else {
            Material material = b.getType();
            byte data = b.getData();
            blockReplaceRunnables.add(new ReplaceBlockRunnable(b, material, data, 10));
        }
    }
}
