package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.gui.impl.ScrollingInventory;
import dev.isnow.ffa.utils.ColorHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryPagingListener implements Listener {

    @EventHandler
    public void onPageEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            final Inventory inventory = event.getInventory();
            final Player clicker = (Player) event.getWhoClicked();
            if(inventory == null || inventory.getTitle() == null || event.getCurrentItem() == null) {
                return;
            }
            if (inventory.getName().equals(ColorHelper.translate(TaklyFFA.INSTANCE.configManager.gui.getString("shop-title")))) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem.getType() == Material.SKULL_ITEM) {
                    if (clickedItem.getItemMeta().getDisplayName().equals(ColorHelper.translate(TaklyFFA.INSTANCE.configManager.gui.getConfigurationSection("shop-buttons").getString("right")))) {
                        if (ScrollingInventory.users.containsKey(clicker.getUniqueId())) {
                            final ScrollingInventory scrollingInventory = ScrollingInventory.users.get(clicker.getUniqueId());
                            if (scrollingInventory.pages.size() > scrollingInventory.getPageNumber() + 1) {
                                scrollingInventory.setPageNumber(scrollingInventory.getPageNumber() + 1);
                                clicker.openInventory(scrollingInventory.pages.get(scrollingInventory.getPageNumber()));
                            }
                            else {
                                clicker.sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getConfigurationSection("shop-messages").getString("no-next-page")));
                            }
                            return;
                        }
                    }
                    if (clickedItem.getItemMeta().getDisplayName().equals(ColorHelper.translate(TaklyFFA.INSTANCE.configManager.gui.getConfigurationSection("shop-buttons").getString("left")))) {
                        if (ScrollingInventory.users.containsKey(clicker.getUniqueId())) {
                            final ScrollingInventory scrollingInventory = ScrollingInventory.users.get(clicker.getUniqueId());
                            if ( scrollingInventory.getPageNumber() > 0) {
                                scrollingInventory.setPageNumber(scrollingInventory.getPageNumber() - 1);
                                clicker.openInventory(scrollingInventory.pages.get(scrollingInventory.getPageNumber()));
                            }
                            else if(scrollingInventory.getPageNumber() == 0){
                                event.getWhoClicked().closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }
}