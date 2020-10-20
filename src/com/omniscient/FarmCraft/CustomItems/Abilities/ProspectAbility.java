package com.omniscient.FarmCraft.CustomItems.Abilities;

import com.omniscient.FarmCraft.CustomItems.Ability;
import com.omniscient.FarmCraft.CustomItems.CustomItem;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.List;

public class ProspectAbility implements Ability {
    int depth;
    String name = "Prospectar";
    String description = "&7Extrai amostra do terreno para análise de traços de minérios até &a'depth' blocos &7abaixo do nível do solo.";
    String input = "CLIQUE DIREITO";
    boolean hidden = false;
    double cooldown = 2;

    public ProspectAbility(int depth) {
        this.depth = depth;
    }

    @Override
    public boolean meetsCondition(Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent e = (PlayerInteractEvent) event;
            Player p = e.getPlayer();
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                List<Material> allowedBlocks = Arrays.asList(Material.DIRT, Material.GRASS, Material.SAND);
                if (allowedBlocks.contains(e.getClickedBlock().getType())) {
                    if (user.getCompany() != null) {
                        for (Terrain terrain : user.getCompany().getTerrains()) {
                            if (terrain.getID().equalsIgnoreCase(p.getWorld().getName())) {
                                return true;
                            }
                        }
                        p.sendMessage(Methods.color("&cVocê só pode utilizar essa ferramenta em um terreno de sua companhia."));
                        return false;
                    } else {
                        p.sendMessage(Methods.color("&cVocê só pode utilizar essa ferramenta em um terreno de sua companhia."));
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void execute(Event event) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Player p = e.getPlayer();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        CustomItem customItem = CustomItem.SOIL;
        ItemStack itemStack = customItem.getItem();
        NBTItem nbtItem = new NBTItem(itemStack);
        for (Terrain terrain : user.getCompany().getTerrains()) {
            if (terrain.getID().equalsIgnoreCase(p.getWorld().getName())) {
                String chunkLocation = p.getLocation().getChunk().getX() + "," + p.getLocation().getChunk().getZ();
                JSONParser parser = new JSONParser();
                try {
                    JSONObject object = (JSONObject) parser.parse(terrain.getResourcesJSON());
                    JSONObject chunkJSON = (JSONObject) object.get(chunkLocation);
                    nbtItem.setString("RESOURCES", chunkJSON.get("resource") + " " + chunkJSON.get("amount") + "/" + chunkJSON.get("amount"));
                    p.getInventory().addItem(nbtItem.getItem());
                } catch (ParseException exception) {
                    p.sendMessage(Methods.color("&cOcorreu um erro."));
                    return;
                }
                Location location = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
                PacketPlayOutWorldParticles particlesPacket = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) 0.5, (float) 0, (float) 0.5, (float) 5, 100, e.getClickedBlock().getType().getId(), e.getClickedBlock().getData());
                for (Player player : p.getWorld().getPlayers()) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particlesPacket);
                }
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
