package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PortalListener implements Listener {
    HashMap<Player, Inventory> portalInventories = new HashMap<>();
    List<Player> eventFired = new ArrayList<>();

    List<Integer> terrainSlots = Arrays.asList(11, 12, 13, 14, 15);
    int closeSlot = 31;

    @EventHandler
    public void onPortalEnter(PlayerPortalEvent e) {
        if (eventFired.contains(e.getPlayer())) {
            return;
        }
        Player p = e.getPlayer();
        eventFired.add(p);
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        TeleportCause teleportCause = e.getCause();
        if (teleportCause == TeleportCause.END_PORTAL) {
            e.setCancelled(true);
            p.teleport(p.getWorld().getSpawnLocation());
            Country country = user.getCountry();
            List<Terrain> terrains = new ArrayList<>();
            if (user.getCompany() == null) {
                Methods.sendActionBar(p, Methods.color("&cVocê precisa fazer parte de uma companhia para possuir terrenos."));
                eventFired.remove(p);
                return;
            }
            if (user.getCompany().getTerrains() == null) {
                Methods.sendActionBar(p, Methods.color("&cSua companhia não tem terrenos nesse país."));
                eventFired.remove(p);
                return;
            }
            for (Terrain terrain : user.getCompany().getTerrains()) {
                if (terrain.getCountry() == country) {
                    terrains.add(terrain);
                }
            }
            if (terrains.size() > 0) {
                p.openInventory(getSelectTerrainInventory(terrains));
                return;
            } else {
                Methods.sendActionBar(p, Methods.color("&cSua companhia não tem terrenos nesse país."));
            }
        } else if (teleportCause == TeleportCause.NETHER_PORTAL) {
            for (Entity entity : p.getNearbyEntities(3, 5, 3)) {
                if (entity instanceof ArmorStand) {
                    ArmorStand armorStand = (ArmorStand) entity;
                    String[] locationParts = armorStand.getCustomName().split(",");
                    if (Bukkit.getWorlds().contains(Bukkit.getWorld(locationParts[0]))) {
                        p.teleport(new Location(Bukkit.getWorld(locationParts[0]), Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3])));
                        p.sendMessage(Methods.color("&aVocê foi teleportado com sucesso!"));
                        eventFired.remove(p);
                        return;
                    }
                }
            }
            p.sendMessage(Methods.color("&cOcorreu um erro, tente novamente mais tarde."));
        }
        eventFired.remove(p);
    }

    @EventHandler
    public void onSelectTerrainInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getName().equals(getSelectTerrainInventory(Collections.emptyList()).getName())) {
                e.setCancelled(true);
                if (e.getSlot() == closeSlot) {
                    p.closeInventory();
                } else if (terrainSlots.contains(e.getSlot())) {
                    ItemStack itemStack = e.getCurrentItem();
                    NBTItem nbtItem = new NBTItem(itemStack);
                    if (nbtItem.hasNBTData()) {
                        if (nbtItem.hasKey("TERRAIN_ID")) {
                            String terrainID = nbtItem.getString("TERRAIN_ID");
                            p.closeInventory();
                            World world = Bukkit.getWorld(terrainID);
                            for (int x = -160; x < world.getWorldBorder().getSize() + 160; x++) {
                                for (int z = -160; z < world.getWorldBorder().getSize() + 160; z++) {
                                    world.setTime(Methods.getHourTick(user.getCountry().getHour(TimeListener.hour)));
                                    world.setBiome(x, z, user.getCountry().getSeason(user.getCountry().getMonth(TimeListener.hour, TimeListener.day, TimeListener.month)).getBiome());
                                }
                            }
                            p.teleport(world.getSpawnLocation());
                        }
                    } else {
                        p.sendMessage(Methods.color("&cEste não é um terreno válido."));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSelectTerrainInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getInventory().getName().equals(getSelectTerrainInventory(Collections.emptyList()).getName())) {
            eventFired.remove(p);
        }
    }

    @EventHandler
    public void onPortalInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        /*if(p.getWorld().getName().equals(user.getCompanyId())){
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.PORTAL){
                Block b = e.getClickedBlock();
                for(Entity entity : p.getNearbyEntities(8,8,8)){
                    if(entity instanceof ArmorStand) {
                        if(entity.getLocation().distance(b.getLocation()) < 5) {
                            Inventory portalInventory = Bukkit.createInventory(null, 27, "Remover portal? ("+b.getLocation().getBlockX()+","+b.getLocation().getBlockY()+","+b.getLocation().getBlockZ()+")");
                            ItemStack itemStack = new ItemStack(Material.STAINED_CLAY, 1, (byte)5);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setLore(Arrays.asList(Methods.color("&7Clique para confirmar a"), Methods.color("&7remoção do portal.")));
                            itemMeta.setDisplayName(Methods.color("&aConfirmar"));
                            itemStack.setItemMeta(itemMeta);
                            portalInventory.setItem(11, itemStack);
                            itemStack = new ItemStack(Material.STAINED_CLAY, 1, (byte)14);
                            itemMeta = itemStack.getItemMeta();
                            itemMeta.setLore(Arrays.asList(Methods.color("&7Clique para cancelar a"), Methods.color("&7remoção do portal.")));
                            itemMeta.setDisplayName(Methods.color("&cCancelar"));
                            itemStack.setItemMeta(itemMeta);
                            portalInventory.setItem(15, itemStack);
                            p.openInventory(portalInventory);
                            portalInventories.put(p, portalInventory);
                        }
                    }
                }
            }
        }*/
    }

    @EventHandler
    public void onPortalInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (portalInventories.containsKey(p)) {
            Inventory portalInventory = portalInventories.get(p);
            if (e.getInventory().getName().startsWith("Remover portal?")) {
                e.setCancelled(true);
                String[] blockLocation = portalInventory.getName().substring(16).replace(")", "").replace("(", "").split(",");
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getSlot() == 11) {
                        p.getWorld().getBlockAt(Integer.parseInt(blockLocation[0]), Integer.parseInt(blockLocation[1]), Integer.parseInt(blockLocation[2])).setType(Material.AIR);
                        p.sendMessage(Methods.color("&aVocê confirmou a remoção do portal."));
                    } else if (e.getSlot() == 15) {
                        p.sendMessage(Methods.color("&cVocê cancelou a remoção do portal."));
                    }
                    p.closeInventory();
                    portalInventories.remove(p);
                }
            }
        }
    }

    public Inventory getSelectTerrainInventory(List<Terrain> terrains) {
        Inventory inventory = Bukkit.createInventory(null, 36, "Seletor de Terrenos");

        ItemStack placeholderItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta placeholderItemMeta = placeholderItemStack.getItemMeta();
        placeholderItemMeta.setDisplayName(" ");
        placeholderItemStack.setItemMeta(placeholderItemMeta);

        ItemStack closeItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
        ItemMeta closeItemMeta = closeItemStack.getItemMeta();
        closeItemMeta.setDisplayName(Methods.color("&cFechar"));
        closeItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Clique aqui para fechar o Seletor de Terrenos.")));
        closeItemStack.setItemMeta(closeItemMeta);

        ItemStack emptyTerrainSlotItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 8);
        ItemMeta emptyTerrainSlotItemMeta = emptyTerrainSlotItemStack.getItemMeta();
        emptyTerrainSlotItemMeta.setDisplayName(Methods.color("&cTerreno vago"));
        emptyTerrainSlotItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Para preencher esse espaço com um novo terreno, adquira-o nesse país.")));
        emptyTerrainSlotItemStack.setItemMeta(emptyTerrainSlotItemMeta);

        int t = 0;
        for (Terrain terrain : terrains) {
            ItemStack terrainSlotItemStack = new ItemStack(Material.STAINED_GLASS_PANE, t + 1, (byte) 5);
            ItemMeta terrainSlotItemMeta = terrainSlotItemStack.getItemMeta();
            terrainSlotItemMeta.setDisplayName(Methods.color("&aTerreno " + (t + 1)));
            List<String> lore = new ArrayList<>();
            lore.addAll(Methods.getLoreLines(Arrays.asList("&7Este espaço de terreno já está ocupado.")));
            lore.add("");
            lore.addAll(Methods.getLoreLines(Arrays.asList(" &fCompanhia: " + terrain.getCompany().getName())));
            lore.addAll(Methods.getLoreLines(Arrays.asList(" &fPaís: " + terrain.getCountry().getName())));
            lore.addAll(Methods.getLoreLines(Arrays.asList(" &fBioma: " + terrain.getTerrainType().getName())));
            lore.add("");
            lore.addAll(Methods.getLoreLines(Arrays.asList("&aClique para viajar até o terreno.")));
            terrainSlotItemMeta.setLore(lore);
            terrainSlotItemStack.setItemMeta(terrainSlotItemMeta);
            NBTItem terrainSlotNBTItem = new NBTItem(terrainSlotItemStack);
            terrainSlotNBTItem.setString("TERRAIN_ID", terrain.getID());
            inventory.setItem(terrainSlots.get(t), terrainSlotNBTItem.getItem());
            t++;
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == closeSlot) {
                inventory.setItem(i, closeItemStack);
            } else if (terrainSlots.contains(i)) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, emptyTerrainSlotItemStack);
                }
            } else {
                inventory.setItem(i, placeholderItemStack);
            }
        }
        return inventory;
    }
}
