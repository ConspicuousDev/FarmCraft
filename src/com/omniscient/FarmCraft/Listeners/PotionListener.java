package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.CustomItems.CustomItem;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class PotionListener implements Listener {
    public static List<Block> brewingStands = new ArrayList<>();
    Map<String, Block> openedBrewingStands = new HashMap<>();
    int recipeSlot = 13;
    int closeSlot = 49;
    List<Integer> potionSlots = Arrays.asList(38, 40, 42);
    List<Integer> timerSlots = Arrays.asList(20, 21, 22, 23, 24, 29, 31, 33);

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent e) {

    }

    @EventHandler
    public void onUseBrewer(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTopInventory().getName().equals(getBrewingInventory("null", Arrays.asList("null", "null", "null")).getName())) {
            e.setCancelled(true);
            if (e.getClickedInventory() != null) {
                if (e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
                    if (e.getSlot() == closeSlot) {
                        p.closeInventory();
                    } else if (e.getSlot() == recipeSlot) {

                    }
                } else {
                    if (e.getClickedInventory().getItem(e.getSlot()) != null) {
                        NBTItem nbtItem = new NBTItem(e.getCurrentItem());
                        if (nbtItem.hasKey("ID")) {
                            if (nbtItem.getBoolean("isPotionIngredient")) {
                                ItemStack clickedItem = e.getCurrentItem().clone();
                                if (p.getOpenInventory().getTopInventory().getItem(recipeSlot) == null) {
                                    if (e.getClickedInventory().getItem(e.getSlot()).getAmount() > 1) {
                                        ItemStack newRecipeItem = clickedItem.clone();
                                        newRecipeItem.setAmount(1);
                                        ItemStack newClickedItem = clickedItem.clone();
                                        newClickedItem.setAmount(clickedItem.getAmount() - 1);
                                        p.getOpenInventory().getTopInventory().setItem(recipeSlot, newRecipeItem);
                                        e.getClickedInventory().setItem(e.getSlot(), newClickedItem);
                                    } else {
                                        p.getOpenInventory().getTopInventory().setItem(recipeSlot, clickedItem);
                                        e.getClickedInventory().clear(e.getSlot());
                                    }
                                }
                            } else if (nbtItem.getBoolean("isPotionFlask")) {
                                ItemStack clickedItem = e.getCurrentItem().clone();
                                for (Integer potionSlot : potionSlots) {
                                    if (p.getOpenInventory().getTopInventory().getItem(potionSlot) == null) {
                                        if (e.getClickedInventory().getItem(e.getSlot()).getAmount() > 1) {
                                            ItemStack newRecipeItem = clickedItem.clone();
                                            newRecipeItem.setAmount(1);
                                            ItemStack newClickedItem = clickedItem.clone();
                                            newClickedItem.setAmount(clickedItem.getAmount() - 1);
                                            p.getOpenInventory().getTopInventory().setItem(potionSlot, newRecipeItem);
                                            e.getClickedInventory().setItem(e.getSlot(), newClickedItem);
                                        } else {
                                            p.getOpenInventory().getTopInventory().setItem(potionSlot, clickedItem);
                                            e.getClickedInventory().clear(e.getSlot());
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBrewerOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();
            if (b.getType().equals(Material.BREWING_STAND)) {
                e.setCancelled(true);
                if (brewingStands.contains(b)) {
                    if (!openedBrewingStands.containsValue(b)) {
                        p.openInventory(getBrewingInventory(b.getMetadata("RecipeSlot").get(0).asString(), Arrays.asList(b.getMetadata("PotionSlot1").get(0).asString(), b.getMetadata("PotionSlot2").get(0).asString(), b.getMetadata("PotionSlot3").get(0).asString())));
                        p.updateInventory();
                        openedBrewingStands.put(p.getDisplayName(), b);
                        Methods.consoleLog(String.valueOf(Arrays.asList(b.getMetadata("PotionSlot1").get(0).asString(), b.getMetadata("PotionSlot2").get(0).asString(), b.getMetadata("PotionSlot3").get(0).asString())));
                    } else {
                        for (String playerName : openedBrewingStands.keySet()) {
                            if (openedBrewingStands.get(playerName).equals(b)) {
                                User user = FarmCraft.onlineUsers.get(playerName);
                                p.sendMessage(Methods.color("&cO jogador " + user.getRank().getTag() + playerName + " &cestá acessando esse Estande de Poções!"));
                                return;
                            }
                        }
                        p.sendMessage(Methods.color("&cOcorreu um erro. Tente novamente mais tarde."));
                    }
                } else {
                    p.sendMessage(Methods.color("&cOcorreu um erro. Tente recolocar esse Estande de Poções no chão."));
                }
            }
        }
    }

    @EventHandler
    public void onBrewerClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inventory = e.getInventory();
        if (inventory.getName().equals(getBrewingInventory("null", Arrays.asList("null", "null", "null")).getName())) {
            Block b = openedBrewingStands.remove(p.getDisplayName());
            if (inventory.getItem(recipeSlot) != null) {
                NBTItem nbtItem = new NBTItem(inventory.getItem(recipeSlot));
                if (nbtItem.hasNBTData()) {
                    if (nbtItem.hasKey("ID")) {
                        b.setMetadata("RecipeSlot", new FixedMetadataValue(FarmCraft.instance, nbtItem.getString("ID")));
                    } else {
                        b.setMetadata("RecipeSlot", new FixedMetadataValue(FarmCraft.instance, "null"));
                    }
                } else {
                    b.setMetadata("RecipeSlot", new FixedMetadataValue(FarmCraft.instance, "null"));
                }
            } else {
                b.setMetadata("RecipeSlot", new FixedMetadataValue(FarmCraft.instance, "null"));
            }
            int i = 0;
            for (Integer potionSlot : potionSlots) {
                i++;
                if (inventory.getItem(potionSlot) != null) {
                    NBTItem nbtItem = new NBTItem(inventory.getItem(potionSlot));
                    if (nbtItem.hasNBTData()) {
                        if (nbtItem.hasKey("ID")) {
                            b.setMetadata("PotionSlot" + i, new FixedMetadataValue(FarmCraft.instance, nbtItem.getString("ID")));
                        } else {
                            b.setMetadata("PotionSlot" + i, new FixedMetadataValue(FarmCraft.instance, "null"));
                        }
                    } else {
                        b.setMetadata("PotionSlot" + i, new FixedMetadataValue(FarmCraft.instance, "null"));
                    }
                } else {
                    b.setMetadata("PotionSlot" + i, new FixedMetadataValue(FarmCraft.instance, "null"));
                }
            }
        }
    }

    @EventHandler
    public void onBrewerPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (b.getType() == Material.BREWING_STAND) {
            b.setMetadata("RecipeSlot", new FixedMetadataValue(FarmCraft.instance, "null"));
            b.setMetadata("PotionSlot1", new FixedMetadataValue(FarmCraft.instance, "null"));
            b.setMetadata("PotionSlot2", new FixedMetadataValue(FarmCraft.instance, "null"));
            b.setMetadata("PotionSlot3", new FixedMetadataValue(FarmCraft.instance, "null"));
            brewingStands.add(b);
        }
    }

    @EventHandler
    public void onBrewerRemove(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (b.getType() == Material.BREWING_STAND) {
            brewingStands.remove(b);
        }
    }

    public Inventory getBrewingInventory(String recipeItem, List<String> potionItems) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Estande de Poções");

        ItemStack placeholderItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta placeholderItemMeta = placeholderItemStack.getItemMeta();
        placeholderItemMeta.setDisplayName(" ");
        placeholderItemStack.setItemMeta(placeholderItemMeta);

        ItemStack timerItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
        ItemMeta timerItemMeta = timerItemStack.getItemMeta();
        timerItemMeta.setDisplayName(" ");
        timerItemStack.setItemMeta(timerItemMeta);

        ItemStack closeItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
        ItemMeta closeItemMeta = closeItemStack.getItemMeta();
        closeItemMeta.setDisplayName(Methods.color("&cFechar"));
        closeItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Clique aqui para fechar o Estande de Poções.")));
        closeItemStack.setItemMeta(closeItemMeta);

        for (int i = 0; i < 54; i++) {
            if (i == recipeSlot) {
                if (!recipeItem.equals("null")) {
                    inventory.setItem(i, CustomItem.valueOf(recipeItem).getItem());
                }
            } else if (i == closeSlot) {
                inventory.setItem(i, placeholderItemStack);
            } else if (potionSlots.contains(i)) {
                int j = potionSlots.indexOf(i);
                if (!potionItems.get(j).equals("null")) {
                    inventory.setItem(i, CustomItem.valueOf(potionItems.get(j)).getItem());
                    Methods.consoleLog(String.valueOf(i + ":" + j));
                }
            } else if (timerSlots.contains(i)) {
                inventory.setItem(i, timerItemStack);
            } else {
                inventory.setItem(i, placeholderItemStack);
            }
        }
        return inventory;
    }
}
