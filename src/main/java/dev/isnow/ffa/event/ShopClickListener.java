package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.data.PlayerData;
import dev.isnow.ffa.data.PlayerDataManager;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.type.ShopItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ShopClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            final Inventory inventory = event.getInventory();
            final Player clicker = (Player) event.getWhoClicked();
            if (inventory == null || inventory.getTitle() == null || event.getCurrentItem() == null) {
                return;
            }
            if (inventory.getName().equals(ColorHelper.translate(TaklyFFA.INSTANCE.configManager.gui.getString("shop-title")))) {
                event.setCancelled(true);
                for(final ShopItemType type : TaklyFFA.INSTANCE.getGuiManager().items) {
                    if(type.getItem().getType() == event.getCurrentItem().getType() && Objects.equals(type.getItem().getItemMeta().getDisplayName(), event.getCurrentItem().getItemMeta().getDisplayName())) {
                        PlayerData data = PlayerDataManager.get(clicker.getUniqueId());
                        if(data.getCoins() >= type.getCost()) {
                            data.setCoins(data.getCoins() - type.getCost());
                            ItemStack edited = type.getItem();
                            ItemMeta meta = type.getItem().getItemMeta();
                            meta.setLore(null);
                            edited.setItemMeta(meta);
                            clicker.getInventory().addItem(edited);
                            clicker.sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getConfigurationSection("shop-messages").getString("successfully-purchased")));
                        }
                        else {
                            clicker.sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getConfigurationSection("shop-messages").getString("not-enough-money")));
                        }
                    }
                }
            }
        }
    }

}
