package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.CustomItems.CustomItem;
import com.omniscient.FarmCraft.CustomItems.ItemManager;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

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
                if (nbtItem.hasNBTData()) {
                    if (nbtItem.getKeys().contains("ID")) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        try {
                            CustomItem customItem = CustomItem.valueOf(nbtItem.getString("ID"));
                            int amount = itemStack.getAmount();
                            itemMeta.setDisplayName(customItem.getItem().getItemMeta().getDisplayName());
                            ItemManager.updateLore(itemStack);
                        } catch (Exception e) {
                            String ID = nbtItem.getString("ID");
                            itemMeta.setDisplayName(Methods.color("&cEste item foi removido."));
                            itemMeta.setLore(Methods.getLoreLines(Arrays.asList("&cO ID desse item era &7" + ID + "&c.", "&cContate um membro da staff caso você ache que isso é um erro.")));
                            nbtItem.removeKey("ID");
                        }
                        itemStack.setItemMeta(itemMeta);
                    }
                }
            }
        }
    }
}
