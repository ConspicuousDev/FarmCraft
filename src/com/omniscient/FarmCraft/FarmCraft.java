package com.omniscient.FarmCraft;

import com.omniscient.FarmCraft.Block.BlockListener;
import com.omniscient.FarmCraft.Block.ReplaceBlockRunnable;
import com.omniscient.FarmCraft.Company.Company;
import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.CustomItems.CustomItem;
import com.omniscient.FarmCraft.CustomItems.Recipe;
import com.omniscient.FarmCraft.Listeners.EntityListener;
import com.omniscient.FarmCraft.Listeners.PotionListener;
import com.omniscient.FarmCraft.Listeners.TimeListener;
import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.Terrain.TerrainType;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Database;
import com.omniscient.FarmCraft.Utils.Definitions;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtinjector.NBTInjector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FarmCraft extends JavaPlugin {
    public static String PREFIX = "&a[&eFarm&fCraft&a]";
    public static String MOTD = "&e&lFARM&f&lCRAFT &b&lCOMPANY &7• 1.8 - 1.16.2\n&2&lABERTURA em breve!";
    public static double version = 1.0;
    public static FarmCraft instance;
    public static Map<String, User> onlineUsers = new HashMap<>();
    public static Map<String, Company> companyList = new HashMap<>();
    public static Map<Recipe, CustomItem> recipeList = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        Database.connect();
        Definitions.listeners();
        Definitions.commands();
        Definitions.dependencies();
        NBTInjector.inject();
        Methods.consoleLog("&aPlugin ativado.");
        try {
            ResultSet resultSet = Database.get("SELECT * FROM company_data");
            while (resultSet.next()) {
                Company company = new Company(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("owner_id"), Arrays.asList(resultSet.getString("member_ids").split(",")), Integer.parseInt(resultSet.getString("bank")));
                companyList.put(company.getID(), company);
            }
        } catch (SQLException e) {
            Methods.consoleLog("&cAs Companies não puderam ser carregadas corretamente!");
        }
        try {
            TimeListener.startClock();
        } catch (SQLException e) {
            Methods.consoleLog("&cO relógio não pôde ser iniciado.");
        }
        try {
            ResultSet resultSet = Database.get("SELECT * FROM terrain_data");
            while (resultSet.next()) {
                Terrain terrain = new Terrain(resultSet.getString("id"), resultSet.getString("company"), Country.valueOf(resultSet.getString("country")), resultSet.getString("resources"), TerrainType.valueOf(resultSet.getString("type")));
                Company company = companyList.get(resultSet.getString("company"));
                company.addTerrain(terrain);
                Methods.loadWorld(terrain.getID(), terrain.getCountry());
            }
        } catch (SQLException e) {
            Methods.consoleLog("&cOs Terrenos não puderam ser carregados corretamente!");
        }
        for (Country country : Country.values()) {
            Methods.loadWorld(country.getID(), country);
        }
        for (CustomItem customItem : CustomItem.values()) {
            if (customItem.getRecipe() != null) {
                recipeList.put(customItem.getRecipe(), customItem);
            }
        }
        Methods.consoleLog("&e" + recipeList.size() + " receita(s) carregada(s).");
        try {
            ResultSet resultSet = Database.get("SELECT * FROM brewing_stands");
            while (resultSet.next()) {
                String[] coordinates = resultSet.getString("coordinates").split(",");
                Methods.consoleLog(Arrays.toString(coordinates));
                Location location = new Location(Bukkit.getWorld(coordinates[0]), Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[2]), Double.parseDouble(coordinates[3]));
                Block b = Bukkit.getWorld(coordinates[0]).getBlockAt(location);
                if (b.getType() == Material.BREWING_STAND) {
                    b.setMetadata("RecipeSlot", new FixedMetadataValue(FarmCraft.instance, resultSet.getString("recipe_slot")));
                    b.setMetadata("PotionSlot1", new FixedMetadataValue(FarmCraft.instance, resultSet.getString("potion_slot_1")));
                    b.setMetadata("PotionSlot2", new FixedMetadataValue(FarmCraft.instance, resultSet.getString("potion_slot_2")));
                    b.setMetadata("PotionSlot3", new FixedMetadataValue(FarmCraft.instance, resultSet.getString("potion_slot_3")));
                    PotionListener.brewingStands.add(b);
                }
            }
            Database.set("DELETE FROM brewing_stands");
            Methods.consoleLog("&e" + PotionListener.brewingStands.size() + " estande(s) de poções carregado(s).");
        } catch (SQLException e) {
            Methods.consoleLog("&cOs Estandes de Poção não puderam ser carregados corretamente!");
        }
    }

    @Override
    public void onDisable() {
        for (ReplaceBlockRunnable runnable : BlockListener.blockReplaceRunnables) {
            runnable.cancel();
            Block b = runnable.getBlock();
            Material material = runnable.getMaterial();
            byte data = runnable.getData();
            b.setType(material);
            b.setData(data);
        }
        for (Block brewingStand : PotionListener.brewingStands) {
            String recipeItem = brewingStand.getMetadata("RecipeSlot").get(0).asString();
            String potionItem1 = brewingStand.getMetadata("PotionSlot1").get(0).asString();
            String potionItem2 = brewingStand.getMetadata("PotionSlot2").get(0).asString();
            String potionItem3 = brewingStand.getMetadata("PotionSlot3").get(0).asString();
            Methods.consoleLog("Saving BrewingStand: " + recipeItem + "," + potionItem1 + "," + potionItem2 + "," + potionItem3 + ".");
            try {
                Database.set("INSERT INTO brewing_stands (coordinates, recipe_slot, potion_slot_1, potion_slot_2, potion_slot_3) VALUES ('" + brewingStand.getLocation().getWorld().getName() + "," + brewingStand.getLocation().getBlockX() + "," + brewingStand.getLocation().getBlockY() + "," + brewingStand.getLocation().getBlockZ() + "', '" + recipeItem + "', '" + potionItem1 + "', '" + potionItem2 + "', '" + potionItem3 + "')");
            } catch (SQLException e) {
                Methods.consoleLog("&cOs dados dos Estandes de Poção não puderam ser salvos corretamente.");
            }
        }
        for (ArmorStand armorStand : EntityListener.damageDisplays) {
            armorStand.remove();
        }
        try {
            Database.set("UPDATE server_data SET year = " + TimeListener.year + ", month = '" + TimeListener.month.name() + "', day = " + TimeListener.day + ", hour = " + TimeListener.hour + ", minute = " + TimeListener.minute + " WHERE id = 1");
        } catch (SQLException e) {
            Methods.consoleLog("&cOs dados das Servidor não puderam ser salvos corretamente.");
        }
        try {
            for (String playerName : onlineUsers.keySet()) {
                Player p = Bukkit.getPlayer(playerName);
                p.closeInventory();
                p.kickPlayer(Methods.color("&e&lFARM&f&lCRAFT &b&lCOMPANY\n\n&cO servidor está sendo reiniciado..."));
                onlineUsers.get(playerName).saveData();
            }
        } catch (SQLException e) {
            Methods.consoleLog("&cOs dados dos Users não puderam ser salvos corretamente.");
        }
        try {
            for (Company company : companyList.values()) {
                company.saveData();
                for (Terrain terrain : company.getTerrains()) {
                    terrain.saveData();
                }
            }
        } catch (SQLException e) {
            Methods.consoleLog("&cOs dados das Companies não puderam ser salvos corretamente.");
        }
        Methods.consoleLog("&cPlugin desativado.");
    }
}
