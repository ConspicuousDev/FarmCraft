package com.omniscient.FarmCraft.CustomItems;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    List<CustomItem> items = new ArrayList<>();
    List<Integer> amounts = new ArrayList<>();

    public Recipe(List<CustomItem> items, List<Integer> amounts) {
        for (CustomItem customItem : items) {
            if (this.items.size() < 10) {
                this.items.add(customItem);
            }
        }
        for (Integer amount : amounts) {
            if (this.amounts.size() < 10) {
                this.amounts.add(amount);
            }
        }
    }

    public List<CustomItem> getItems() {
        return this.items;
    }

    public List<Integer> getAmounts() {
        return this.amounts;
    }
}
