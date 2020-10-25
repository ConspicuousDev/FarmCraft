package com.omniscient.FarmCraft.Utils;

import com.omniscient.FarmCraft.Company.Company;
import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Listeners.TimeListener;
import com.omniscient.FarmCraft.Terrain.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

public class Methods {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void consoleLog(String string) {
        Bukkit.getConsoleSender().sendMessage(color(FarmCraft.PREFIX + " &f" + string));
    }

    public static List<String> getLoreLines(List<String> loreParts) {
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
        itemMeta.setLore(getLoreLines(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*public static void pasteSchematic(Location location){
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "/schematics/farm.schematic");
        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
        try{
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.paste(session, new Vector(location.getX(), location.getY(), location.getZ()), false);
        }
        catch (DataException | IOException | MaxChangedBlocksException e){
            e.printStackTrace();
        }
    }*/
    public static Company createCompany(String name, Player owner) throws SQLException {
        String companyID = UUID.randomUUID().toString();
        Company company = new Company(companyID, name, owner.getUniqueId().toString(), Arrays.asList(), 0);
        Database.set("INSERT INTO company_data (id, name, owner_id, member_ids, terrains, bank) VALUES ('" + company.getID() + "', '" + company.getName() + "', '" + company.getOwnerID() + "', '', '', '" + company.getBank() + "')");
        FarmCraft.companyList.put(company.getID(), company);
        return company;
    }

    public static Terrain createTerrain(Company company, Country country) throws SQLException {
        String terrainID = UUID.randomUUID().toString();
        TerrainType terrainType = country.getRandomTerrainType();
        Terrain terrain = new Terrain(terrainID, company.getID(), country, "", terrainType);
        WorldCreator worldCreator = new WorldCreator(terrainID);
        worldCreator.generator(new TerrainGenerator(worldCreator.seed(), terrainType));
        World world = Bukkit.createWorld(worldCreator);
        world.getWorldBorder().setCenter(127.5, 127.5);
        world.getWorldBorder().setSize(255);
        world.setGameRuleValue("doDaylightCycle", "true");
        world.setGameRuleValue("randomTickSpeed", "0");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setSpawnLocation(127, 65, 127);
        List<Resource> possibleResources = country.getTerrainResources().get(terrainType);
        Map<String, ResourceNode> resourceMap = new HashMap<>();
        Map<Resource, Integer> ranges = new HashMap<>();
        int totalSum = 0;
        for (Resource resource : possibleResources) {
            totalSum += resource.getWeight();
        }
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int random = new Random().nextInt(totalSum);
                int sum = 0;
                int i = 0;
                while (sum < random) {
                    sum += possibleResources.get(i++).getWeight();
                }
                Resource selectedResource = possibleResources.get(Math.max(0, i - 1));
                resourceMap.put(x + "," + z, new ResourceNode(selectedResource, new Random().nextInt(selectedResource.getMaxNode() - selectedResource.getMinNode()) + selectedResource.getMinNode()));
            }
        }
        JSONObject JSON = new JSONObject();
        for (String chunkLocation : resourceMap.keySet()) {
            ResourceNode node = resourceMap.get(chunkLocation);
            Resource resource = node.getResource();
            JSONObject nodeJSON = new JSONObject();
            nodeJSON.put("resource", resource.getID());
            nodeJSON.put("amount", node.getCap());
            JSON.put(chunkLocation, nodeJSON);
        }
        terrain.setResourcesJSON(JSON.toString());
        Database.set("INSERT INTO terrain_data (id, company, country, resources) VALUES ('" + terrain.getID() + "', '" + terrain.getCompany().getID() + "', '" + terrain.getCountry().name() + "', '" + terrain.getResourcesJSON() + "')");
        company.addTerrain(terrain);
        return terrain;
    }

    public static void sendActionBar(Player p, String message) {
        String s = color(message);
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    public static int getHourTick(int hour) {
        if (hour > 5) {
            return hour * 1000 - 6000;
        } else {
            return hour * 1000 + 18000;
        }
    }

    public static void loadWorld(String worldName, Country country) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        Bukkit.createWorld(worldCreator);
        World world = Bukkit.getWorld(worldName);
        world.setGameRuleValue("doDaylightCycle", "true");
        world.setGameRuleValue("randomTickSpeed", "0");
        world.setGameRuleValue("doMobSpawning", "false");
        if (TimeListener.hour > 5) {
            world.setTime(getHourTick(country.getHour(TimeListener.hour)));
        } else {
            world.setTime(getHourTick(country.getHour(TimeListener.hour)));
        }
    }
}
