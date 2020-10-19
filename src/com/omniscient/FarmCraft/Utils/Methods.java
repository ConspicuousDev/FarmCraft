package com.omniscient.FarmCraft.Utils;

import com.omniscient.FarmCraft.FarmCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Methods {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void consoleLog(String string) {
        Bukkit.getConsoleSender().sendMessage(color(FarmCraft.PREFIX + " &f" + string));
    }

    public static List<String> getLore(List<String> loreParts) {
        int maxChars = 40;
        List<String> loreLines = new ArrayList<>();
        for (String s : loreParts) {
            List<String> loreWords = Arrays.asList(s.split(" "));
            String defaultColor = "&7";
            if (loreWords.get(0).startsWith("&")) {
                defaultColor = loreWords.get(0).substring(0, 2);
            }
            int totalLines = s.length() / maxChars;
            int nextWord = 0;
            if (totalLines == 0 && !loreWords.get(0).equalsIgnoreCase("") && loreWords.size() == 1) {
                totalLines++;
            }
            for (int i = 0; i < totalLines + 1; i++) {
                String loreLine = defaultColor;
                for (int j = nextWord; j < loreWords.size(); j++) {
                    if (loreLine.length() + loreWords.get(j).length() + 1 <= maxChars) {
                        loreLine += loreWords.get(j) + " ";
                        if (j == loreWords.size() - 1) {
                            loreLines.add(Methods.color(loreLine));
                            nextWord = j;
                            break;
                        }
                    } else {
                        loreLines.add(Methods.color(loreLine));
                        nextWord = j;
                        break;
                    }
                }
            }
        }
        return loreLines;
    }

    public static String formatNumber(int n) {
        DecimalFormat format = new DecimalFormat("###,###");
        return String.valueOf(format.format(n)).replace(",", ".");
    }

    public static ItemStack createIcon(String name, List<String> lore, Material material, int b) {
        ItemStack itemStack = new ItemStack(material, 1, (byte) b);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(color(name));
        itemMeta.setLore(getLore(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
