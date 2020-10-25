package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.CustomItems.CustomItem;
import com.omniscient.FarmCraft.CustomItems.Recipe;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCraftListener implements Listener {
    int outputSlot = 23;
    int closeSlot = 49;
    List<Integer> quickCraftingSlots = Arrays.asList(16, 25, 34);
    List<Integer> recipeIndicatorSlots = Arrays.asList(45, 46, 47, 48, 50, 51, 52, 53);
    List<Integer> tableSlots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);

    @EventHandler
    public void onCraftingBenchInteract(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getName().equals(getCraftingInventory().getName())) {
                if (e.getSlot() == outputSlot) {
                    if (!e.getInventory().getItem(outputSlot).getItemMeta().getDisplayName().equals(Methods.color("&cA receita não foi encontrada!"))) {
                        ItemStack recipeNotFoundItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
                        ItemMeta recipeNotFoundItemMeta = recipeNotFoundItemStack.getItemMeta();
                        recipeNotFoundItemMeta.setDisplayName(Methods.color("&cA receita não foi encontrada!"));
                        recipeNotFoundItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Para ver todas as receitas, utilize o &aMenu&7 no seu inventário.")));
                        recipeNotFoundItemStack.setItemMeta(recipeNotFoundItemMeta);
                        e.getClickedInventory().setItem(outputSlot, recipeNotFoundItemStack);
                        p.updateInventory();
                    } else {
                        e.setCancelled(true);
                    }
                } else if (tableSlots.contains(e.getSlot())) {
                    List<CustomItem> tableItems = new ArrayList<>();
                    List<Integer> tableAmounts = new ArrayList<>();
                    for (Integer tableSlot : tableSlots) {
                        ItemStack itemStack = e.getClickedInventory().getItem(tableSlot);
                        if (itemStack != null) {
                            NBTItem nbtItem = new NBTItem(itemStack);
                            if (nbtItem.hasNBTData()) {
                                if (nbtItem.getKeys().contains("ID")) {
                                    tableItems.add(CustomItem.valueOf(nbtItem.getString("ID")));
                                    tableAmounts.add(e.getClickedInventory().getItem(tableSlot).getAmount());
                                } else {
                                    tableItems.add(null);
                                    tableAmounts.add(null);
                                }
                            } else {
                                tableItems.add(null);
                                tableAmounts.add(null);
                            }
                        } else {
                            tableItems.add(null);
                            tableAmounts.add(null);
                        }
                    }
                    Recipe tableRecipe = new Recipe(tableItems, tableAmounts);
                    for (Recipe recipe : FarmCraft.recipeList.keySet()) {
                        if (recipe.equals(tableRecipe)) {
                            Methods.consoleLog(FarmCraft.recipeList.get(recipe).name());
                            CustomItem outputItem = FarmCraft.recipeList.get(recipe);
                            e.getClickedInventory().setItem(outputSlot, outputItem.getItem());
                            ItemStack rightIndicatorItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                            ItemMeta rightIndicatorItemMeta = rightIndicatorItemStack.getItemMeta();
                            rightIndicatorItemMeta.setDisplayName(Methods.color("&aReceita Correta!"));
                            rightIndicatorItemStack.setItemMeta(rightIndicatorItemMeta);
                            for (Integer indicatorSlot : recipeIndicatorSlots) {
                                e.getClickedInventory().setItem(indicatorSlot, rightIndicatorItemStack);
                            }
                            p.updateInventory();
                            return;
                        }
                    }
                    ItemStack recipeNotFoundItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
                    ItemMeta recipeNotFoundItemMeta = recipeNotFoundItemStack.getItemMeta();
                    recipeNotFoundItemMeta.setDisplayName(Methods.color("&cA receita não foi encontrada!"));
                    recipeNotFoundItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Para ver todas as receitas, utilize o &aMenu&7 no seu inventário.")));
                    recipeNotFoundItemStack.setItemMeta(recipeNotFoundItemMeta);
                    e.getClickedInventory().setItem(outputSlot, recipeNotFoundItemStack);
                    p.updateInventory();
                } else if (e.getSlot() == closeSlot) {
                    e.setCancelled(true);
                    p.closeInventory();
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCraftingBenchOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.WORKBENCH) {
                e.setCancelled(true);
                p.openInventory(getCraftingInventory());
            }
        }
    }

    @EventHandler
    public void onCraftingBenchClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getInventory().getName().equals(getCraftingInventory().getName())) {
            for (Integer slot : tableSlots) {
                ItemStack recipeItemStack = e.getInventory().getItem(slot);
                if (recipeItemStack != null) {
                    p.getInventory().addItem(recipeItemStack);
                }
            }
        }
    }

    public Inventory getCraftingInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mesa de Trabalho");

        ItemStack placeholderItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta placeholderItemMeta = placeholderItemStack.getItemMeta();
        placeholderItemMeta.setDisplayName(" ");
        placeholderItemStack.setItemMeta(placeholderItemMeta);

        ItemStack comingSoonItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
        ItemMeta comingSoonItemMeta = comingSoonItemStack.getItemMeta();
        comingSoonItemMeta.setDisplayName(Methods.color("&cEm Breve"));
        comingSoonItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Esta utilidade chegará logo ao servidor.")));
        comingSoonItemStack.setItemMeta(comingSoonItemMeta);

        ItemStack closeItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
        ItemMeta closeItemMeta = closeItemStack.getItemMeta();
        closeItemMeta.setDisplayName(Methods.color("&cFechar"));
        closeItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Clique aqui para fechar a Mesa de Trabalho.")));
        closeItemStack.setItemMeta(closeItemMeta);

        ItemStack wrongIndicatorItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta wrongIndicatorItemMeta = wrongIndicatorItemStack.getItemMeta();
        wrongIndicatorItemMeta.setDisplayName(Methods.color("&cReceita Incorreta!"));
        wrongIndicatorItemStack.setItemMeta(wrongIndicatorItemMeta);

        ItemStack recipeNotFoundItemStack = new ItemStack(Material.BARRIER, 1, (byte) 0);
        ItemMeta recipeNotFoundItemMeta = recipeNotFoundItemStack.getItemMeta();
        recipeNotFoundItemMeta.setDisplayName(Methods.color("&cA receita não foi encontrada!"));
        recipeNotFoundItemMeta.setLore(Methods.getLoreLines(Arrays.asList("&7Para ver todas as receitas, utilize o &aMenu&7 no seu inventário.")));
        recipeNotFoundItemStack.setItemMeta(recipeNotFoundItemMeta);

        for (int i = 0; i < 54; i++) {
            if (tableSlots.contains(i)) {
                //air
            } else if (quickCraftingSlots.contains(i)) {
                inventory.setItem(i, comingSoonItemStack);
            } else if (recipeIndicatorSlots.contains(i)) {
                inventory.setItem(i, wrongIndicatorItemStack);
            } else if (i == outputSlot) {
                inventory.setItem(i, recipeNotFoundItemStack);
            } else if (i == closeSlot) {
                inventory.setItem(i, closeItemStack);
            } else {
                inventory.setItem(i, placeholderItemStack);
            }
        }


        return inventory;
    }

    public List<ItemStack> getTableItems(Player p) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Integer i : tableSlots) {
            itemStacks.add(p.getOpenInventory().getItem(i));
        }
        return itemStacks;
    }
}
