package dev.isnow.ffa.utils.type;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ShopItemType{
    ItemStack item;
    int cost;

    public ShopItemType(ItemStack item, int cost) {
        this.item = item;
        this.cost = cost;
    }

}
