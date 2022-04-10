package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.type.LimitType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickItemListener implements Listener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Material mat = e.getItem().getItemStack().getType();
        short data = e.getItem().getItemStack().getDurability();

        LimitType created = new LimitType(mat, data);

        if(TaklyFFA.INSTANCE.limitTypeArrayList.contains(created)) {
            e.setCancelled(true);
        }
    }
}
