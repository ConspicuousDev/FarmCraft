package com.omniscient.FarmCraft.CustomItems;

import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {
    public static void addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasNBTData()) {
            if (nbtItem.hasKey("ID")) {
                String enchantmentsString = nbtItem.getString("ENCHANTMENTS");
                if (!enchantmentsString.endsWith(",") && !enchantmentsString.equalsIgnoreCase("")) {
                    enchantmentsString += ",";
                }
                enchantmentsString += enchantment.name() + ":" + level;
                Methods.consoleLog(enchantmentsString);
                nbtItem.setString("ENCHANTMENTS", enchantmentsString);
                updateLore(nbtItem;);
            }
        }
    }

    public static void updateLore(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasNBTData()) {
            if (nbtItem.hasKey("ID")) {
                CustomItem customItem = CustomItem.valueOf(nbtItem.getString("ID"));
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (customItem.getDescription() != null) {
                    lore.addAll(Methods.getLoreLines(Arrays.asList(customItem.getDescription())));
                }
                lore.add("");
                String loreEnchantments = "";
                String[] enchantments = nbtItem.getString("ENCHANTMENTS").split(",");
                //List<String> removeEnchantments = new ArrayList<>();
                for (String enchantmentString : enchantments) {
                    Methods.consoleLog(enchantmentString);
                    String[] enchantmentData = enchantmentString.split(":");
                    try {
                        Enchantment enchantment = Enchantment.valueOf(enchantmentData[0]);
                        int level = Integer.parseInt(enchantmentData[1]);
                        loreEnchantments += "&9" + enchantment.getName() + " " + level + ",";
                    } catch (Exception e) {
                        //removeEnchantments.add(enchantmentString);
                    }
                }
                if (loreEnchantments.endsWith(",")) {
                    loreEnchantments = loreEnchantments.substring(0, loreEnchantments.length() - 1);
                }
                if (!loreEnchantments.equalsIgnoreCase("")) {
                    lore.addAll(Methods.getLoreLines(Arrays.asList(loreEnchantments)));
                    lore.add("");
                }
                for (Ability ability : customItem.getAbilities()) {
                    if (!ability.isHidden()) {
                        lore.add(Methods.color("&6") + ability.getName() + " " + Methods.color("&e&l") + ability.getInput());
                        lore.addAll(Methods.getLoreLines(Arrays.asList(ability.getDescription())));
                        lore.add(Methods.color("&8Cooldown: " + ability.getCooldown() + "s"));
                        lore.add("");
                    }
                }
                lore.add("");
                ItemType itemType = customItem.getItemType();
                try {
                    itemType = ItemType.valueOf(nbtItem.getString("ITEM_TYPE"));
                } catch (Exception e) {
                    nbtItem.setString("ITEM_TYPE", customItem.getItemType().name());
                }
                ItemRarity rarity = customItem.getRarity();
                try {
                    rarity = ItemRarity.valueOf(nbtItem.getString("RARITY"));
                } catch (Exception e) {
                    nbtItem.setString("RARITY", rarity.name());
                }
                lore.add(rarity.getColor() + Methods.color("&l") + itemType.getName() + rarity.getName());
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }
}
