package dev.isnow.ffa.gui.impl;

import com.google.common.collect.Iterables;
import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.type.ShopItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ScrollingInventory {

    private final Player clicker;
    private final ArrayList<ShopItemType> items;
    private int pageNumber;
    private final String title;

    public static HashMap<UUID, ScrollingInventory> users = new HashMap<>();

    public ArrayList<Inventory> pages;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ScrollingInventory(Player clicker, ArrayList<ShopItemType> items, String title){
        this.clicker = clicker;
        this.pages = new ArrayList<>();
        this.items = items;
        this.title = title;
        pageNumber = 0;
    }

    public void showInventory(){
        Inventory inventory = getBlankPage();
        int slot = 9;
        for(final ShopItemType item : items){
            if (inventory.getItem(slot) != null) slot++;

            if(inventory.contains(item.getItem())) continue;
            inventory.setItem(slot, item.getItem());
            if(slot == 44 && isEmpty(slot, inventory)){
                pages.add(inventory);
                inventory = getBlankPage();
                slot = 9;
                if (otherPagesContain(item.getItem())) continue;
                if(isEmpty(slot, inventory)) slot++;
                inventory.setItem(slot, item.getItem());

            }
        }

        pages.add(inventory);
        users.put(clicker.getUniqueId(), this);
        Inventory inv = Iterables.getLast(pages);
        ItemStack skullRight = new ItemStack(Material.SKULL_ITEM, (byte) 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) skullRight.getItemMeta();
        skullMeta.setOwner("MHF_ArrowRight");
        skullMeta.setDisplayName("Nast");
        skullRight.setItemMeta(skullMeta);
        inv.remove(skullRight);
        clicker.openInventory(pages.get(0));

    }

    private boolean isEmpty(int slot, Inventory inventory){
        return inventory.getItem(slot) != null && inventory.getItem(slot).getType() != Material.AIR;
    }

    private boolean otherPagesContain(ItemStack item){
        for(final Inventory page : pages){
            if(page.contains(item)) return true;
        }

        return false;
    }

    private Inventory getBlankPage(){
        Inventory blankPage;
        blankPage =  Bukkit.createInventory(null, 54, title);

        for (int i = 0; i < 9; i++) {
            ItemStack gray = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
            ItemMeta im = gray.getItemMeta();
            im.setDisplayName("§7");
            gray.setItemMeta(im);
            blankPage.setItem(i, gray);
        }
        ItemStack skullRight = new ItemStack(Material.SKULL_ITEM, (byte) 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) skullRight.getItemMeta();
        skullMeta.setOwner("MHF_ArrowRight");
        skullMeta.setDisplayName(ColorHelper.translate(TaklyFFA.INSTANCE.configManager.gui.getConfigurationSection("shop-buttons").getString("right")));
        skullRight.setItemMeta(skullMeta);
        blankPage.setItem(53, skullRight);
        ItemStack skullLeft = new ItemStack(Material.SKULL_ITEM, (byte) 1, (byte) 3);
        if(pageNumber != pages.size()) {
            SkullMeta leftMeta = (SkullMeta) skullLeft.getItemMeta();
            leftMeta.setDisplayName(ColorHelper.translate(TaklyFFA.INSTANCE.configManager.gui.getConfigurationSection("shop-buttons").getString("left")));
            leftMeta.setOwner("MHF_ArrowLeft");
            skullLeft.setItemMeta(leftMeta);
            blankPage.setItem(45, skullLeft);
        }

        return blankPage;
    }

}