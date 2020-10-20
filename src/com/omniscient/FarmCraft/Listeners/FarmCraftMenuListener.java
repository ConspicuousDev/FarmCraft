package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.Calendar.CalendarInventory;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FarmCraftMenuListener implements Listener {
    public static ItemStack getMenuItem() {
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Methods.color("&aMenu &7(Clique Direito)"));
        itemMeta.setLore(Arrays.asList(Methods.color("&7Clique para acessar funções"), Methods.color("&7itens e seu progresso no"), Methods.color("&7FarmCraft!")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Inventory getMenuInventory(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Menu");
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getSlot();
        if (e.getClickedInventory() != null) {
            if (itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().getDisplayName().equals(Methods.color("&aMenu &7(Clique Direito)"))) {
                    e.setCancelled(true);
                    p.getInventory().setItem(8, FarmCraftMenuListener.getMenuItem());
                    p.openInventory(getMenuInventory(p));
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().getDisplayName().equals(Methods.color("&aMenu &7(Clique Direito)"))) {
                e.setCancelled(true);
                //p.openInventory(getMenuInventory(p));
                p.openInventory(CalendarInventory.getCalendarInventory(FarmCraft.onlineUsers.get(p.getDisplayName())));
            }
        }
    }
}
