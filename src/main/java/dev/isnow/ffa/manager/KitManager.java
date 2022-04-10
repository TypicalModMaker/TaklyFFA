package dev.isnow.ffa.manager;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter@Setter
public class KitManager {

    private final TaklyFFA plugin = TaklyFFA.INSTANCE;

    public ArrayList<ItemStack> armor = new ArrayList<>();

    public HashMap<ItemStack, Integer> items = new HashMap<>();


    public KitManager() {
        this.loadConfig();
    }

    private void loadConfig() {
        final FileConfiguration config = this.plugin.configManager.kit;

        if (config.contains("equipment")) {
            try {
                for(String s : config.getConfigurationSection("equipment").getKeys(false)) {
                    ConfigurationSection cs = config.getConfigurationSection("equipment").getConfigurationSection(s);
                    Material itemMat = Material.getMaterial(cs.getString("material"));
                    int itemAmount = cs.getInt("amount");
                    ItemStack is = new ItemStack(itemMat, itemAmount);
                    List<String> enchStrings = cs.getStringList("enchantments");
                    for(String s1 : enchStrings) {
                        Enchantment ench = Enchantment.getByName(s1.split(":")[0]);
                        is = new ItemBuilder(is).enchantment(ench, Integer.parseInt(s1.split(":")[1])).build();
                    }
                    this.armor.add(is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (config.contains("items")) {
            try {
                for(String s : config.getConfigurationSection("items").getKeys(false)) {
                    ConfigurationSection cs = config.getConfigurationSection("items").getConfigurationSection(s);
                    Material itemMat = Material.getMaterial(cs.getString("material"));
                    int itemAmount = cs.getInt("amount");
                    ItemStack is = new ItemStack(itemMat, itemAmount);
                    List<String> enchStrings = cs.getStringList("enchantments");
                    for(String s1 : enchStrings) {
                        Enchantment ench = Enchantment.getByName(s1.split(":")[0]);
                        is = new ItemBuilder(is).enchantment(ench, Integer.parseInt(s1.split(":")[1])).build();
                    }
                    int slot = cs.getInt("slot");
                    this.items.put(is, slot);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
