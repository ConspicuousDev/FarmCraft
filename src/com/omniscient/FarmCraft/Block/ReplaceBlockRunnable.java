package com.omniscient.FarmCraft.Block;

import com.omniscient.FarmCraft.FarmCraft;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static com.omniscient.FarmCraft.Block.BlockListener.blockReplaceRunnables;

public class ReplaceBlockRunnable {
    int ID;
    Block b;
    Material material;
    byte data;

    public ReplaceBlockRunnable(Block b, Material material, byte data, int delay) {
        this.b = b;
        this.material = material;
        this.data = data;
        this.ID = Bukkit.getScheduler().scheduleSyncDelayedTask(FarmCraft.instance, new Runnable() {
            @Override
            public void run() {
                b.setType(material);
                b.setData(data);
                b.getWorld().playSound(b.getLocation(), Sound.DIG_WOOD, 1, 1);
                PacketPlayOutWorldParticles particlesPacket = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), (float) 0.5, (float) 0, (float) 0.5, (float) 5, 100, material.getId(), data);
                for (Player player : b.getWorld().getPlayers()) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particlesPacket);
                }
            }
        }, delay * 20);
        blockReplaceRunnables.remove(this);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(ID);
    }

    public int getID() {
        return ID;
    }

    public Block getBlock() {
        return b;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }
}
