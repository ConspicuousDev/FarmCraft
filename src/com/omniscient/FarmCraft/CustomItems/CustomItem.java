package com.omniscient.FarmCraft.CustomItems;

import com.omniscient.FarmCraft.CustomItems.Abilities.GrappleAbility;
import com.omniscient.FarmCraft.CustomItems.Abilities.NotPlaceableAbility;
import com.omniscient.FarmCraft.CustomItems.Abilities.ProspectAbility;
import com.omniscient.FarmCraft.CustomItems.Abilities.SayNBTAbility;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CustomItem {
    STONE_RESOURCE("Pedra", null, Arrays.asList(new NotPlaceableAbility(), new SayNBTAbility()), Material.COBBLESTONE, (byte) 0, ItemRarity.COMMON, ItemType.RESOURCE, null),
    POTION("Poção", null, Arrays.asList(), Material.POTION, (byte) 0, ItemRarity.COMMON, ItemType.NONE, null),
    GRAPPLING_HOOK("Gancho", null, Arrays.asList(new GrappleAbility()), Material.FISHING_ROD, (byte) 0, ItemRarity.UNCOMMON, ItemType.NONE, null),
    SOIL("Amostra de Solo", "&7Utilize um &bAnalizador de Terreno &7para verificar a existência de minerais nessa terra.", Arrays.asList(new NotPlaceableAbility(), new SayNBTAbility()), Material.DIRT, (byte) 0, ItemRarity.COMMON, ItemType.NONE, null),
    ADMIN_ITEM("Admin Item", "&7Este item, forjado pelos fundadores desta terra, pertence somente a &cAdmins &7e &6Masters&7!", Arrays.asList(new NotPlaceableAbility()), Material.BEDROCK, (byte) 0, ItemRarity.SPECIAL, ItemType.NONE, null),
    PROSPECTOR_1000("Prospectora 1000", "&7Descubra a existência de minérios em seus terrenos para ficar rico!", Arrays.asList(new ProspectAbility(16), new SayNBTAbility()), Material.DIAMOND_PICKAXE, (byte) 0, ItemRarity.UNCOMMON, ItemType.TOOL, new Recipe(Arrays.asList(null, null, null, null, ADMIN_ITEM, null, null, null, null), Arrays.asList(null, null, null, null, 1, null, null, null, null))),
    PROSPECTOR_2000("Prospectora 2000", "&7Descubra a existência de minérios em seus terrenos para ficar rico!", Arrays.asList(new ProspectAbility(32)), Material.DIAMOND_PICKAXE, (byte) 0, ItemRarity.RARE, ItemType.TOOL, new Recipe(Arrays.asList(null, null, null, null, ADMIN_ITEM, null, null, null, null), Arrays.asList(null, null, null, null, 1, null, null, null, null))),
    PROSPECTOR_3000("Prospectora 3000", "&7Descubra a existência de minérios em seus terrenos para ficar rico!", Arrays.asList(new ProspectAbility(64)), Material.DIAMOND_PICKAXE, (byte) 0, ItemRarity.EPIC, ItemType.TOOL, new Recipe(Arrays.asList(null, null, null, null, ADMIN_ITEM, null, null, null, null), Arrays.asList(null, null, null, null, 1, null, null, null, null)));

    String id;
    String name;
    String description;
    List<Ability> abilities;
    Material material;
    byte b;
    ItemRarity rarity;
    ItemType itemType;
    Recipe recipe;

    CustomItem(String name, String description, List<Ability> abilities, Material material, byte b, ItemRarity rarity, ItemType itemType, Recipe recipe) {
        this.id = name();
        this.name = name;
        this.description = description;
        this.abilities = abilities;
        this.material = material;
        this.b = b;
        this.rarity = rarity;
        this.itemType = itemType;
        this.recipe = recipe;
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material, 1, b);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (name != null) {
            itemMeta.setDisplayName(rarity.getColor() + name);
        }
        List<String> lore = new ArrayList<>();
        if (description != null) {
            lore.addAll(Methods.getLoreLines(Arrays.asList(description)));
        }
        lore.add("");
        for (Ability ability : abilities) {
            if (!ability.isHidden()) {
                lore.add(Methods.color("&6") + ability.getName() + " " + Methods.color("&e&l") + ability.getInput());
                lore.addAll(Methods.getLoreLines(Arrays.asList(ability.getDescription())));
                lore.add(Methods.color("&8Cooldown: " + ability.getCooldown() + "s"));
                lore.add("");
            }
        }
        String itemType = "";
        if (this.itemType != ItemType.NONE) {
            itemType = this.itemType.getName() + " ";
        }
        lore.add(rarity.getColor() + Methods.color("&l") + itemType + rarity.getName());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger("Unbreakable", 1);
        nbtItem.setInteger("HideFlags", 127);
        nbtItem.setString("ID", this.id);
        nbtItem.setString("ENCHANTMENTS", "");
        nbtItem.setString("RARITY", this.rarity.name());
        nbtItem.setString("ITEM_TYPE", this.itemType.name());
        return nbtItem.getItem();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getByte() {
        return b;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public ItemType getItemType() {
        return this.itemType;
    }
}
