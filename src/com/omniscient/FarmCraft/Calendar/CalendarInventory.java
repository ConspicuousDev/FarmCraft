package com.omniscient.FarmCraft.Calendar;

import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.Countries.Month;
import com.omniscient.FarmCraft.Listeners.TimeListener;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CalendarInventory {
    public static Inventory getCalendarInventory(User user) {
        Country country = user.getCountry();
        Month month = country.getMonth(TimeListener.hour, TimeListener.day, TimeListener.month);
        Inventory inventory = Bukkit.createInventory(null, 54, "Calendário - " + month.getName() + " (" + country.getName() + ")");
        int day = country.getDay(TimeListener.hour, TimeListener.day, TimeListener.month);
        int offset = 0;
        for (int slot = 0; slot < 28; slot++) {
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(Methods.color("&a" + day));
            itemStack.setItemMeta(itemMeta);
            int currentSlot = slot + 10;
            if (currentSlot == 17 || currentSlot == 24 || currentSlot == 31) {
                offset += 2;
            }
            inventory.setItem(currentSlot + offset, itemStack);
            day++;
        }
        return inventory;
    }
}
