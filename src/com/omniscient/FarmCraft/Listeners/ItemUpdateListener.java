package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.CustomItems.CustomItem;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUpdateListener implements Listener {
    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        updateInventory(p);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = (Player) e.getPlayer();
        updateInventory(p);
    }

    public void updateInventory(Player p) {
        Inventory inventory = p.getInventory();
        int i = -1;
        for (ItemStack itemStack : inventory.getContents()) {
            i++;
            if (itemStack != null) {
                NBTItem nbtItem = new NBTItem(itemStack);
                if (nbtItem.hasNBTData() && nbtItem.getKeys().contains("ID")) {
                    int amount = itemStack.getAmount();
                    CustomItem customItem = CustomItem.valueOf(nbtItem.getString("ID"));
                    ItemStack customItemStack = customItem.getItem();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(customItemStack.getItemMeta().getDisplayName());
                    itemMeta.setLore(customItemStack.getItemMeta().getLore());
                    itemStack.setItemMeta(itemMeta);
                }
            }
        }
    }
}
