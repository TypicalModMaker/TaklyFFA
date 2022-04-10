package dev.isnow.ffa.gui;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.gui.impl.ScrollingInventory;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.ItemBuilder;
import dev.isnow.ffa.utils.type.ShopItemType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {

    private final TaklyFFA plugin = TaklyFFA.INSTANCE;
    public final ArrayList<ShopItemType> items = new ArrayList<>();

    public GuiManager() {
        try {
            for(final String s : plugin.getConfigManager().gui.getConfigurationSection("shop-items").getKeys(false)) {
                ConfigurationSection cs = plugin.getConfigManager().gui.getConfigurationSection("shop-items").getConfigurationSection(s);
                Material itemMat = Material.getMaterial(cs.getString("material"));
                String name = ColorHelper.translate(cs.getString("name"));
                short meta = Short.parseShort(String.valueOf(cs.getInt("metadata")));
                int itemAmount = cs.getInt("amount");
                ItemStack is = new ItemBuilder(new ItemStack(itemMat, itemAmount, meta)).name(name).build();
                List<String> enchStrings = cs.getStringList("enchantments");
                for(String s1 : enchStrings) {
                    Enchantment ench = Enchantment.getByName(s1.split(":")[0]);
                    is = new ItemBuilder(is).enchantment(ench, Integer.parseInt(s1.split(":")[1])).build();
                }
                final List<String> loreList = cs.getStringList("lore");
                final List<String> loreListFormatted = new ArrayList<>();
                for(final String s1 : loreList) {
                    loreListFormatted.add(ColorHelper.translate(s1));
                }
                ItemMeta im = is.getItemMeta();
                im.setLore(loreListFormatted);
                is.setItemMeta(im);
                items.add(new ShopItemType(is, cs.getInt("price")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openGui(Player p) {

        ScrollingInventory scrollingInventory = new ScrollingInventory(p, items, ColorHelper.translate(plugin.configManager.gui.getString("shop-title")));
        scrollingInventory.showInventory();
    }
}
