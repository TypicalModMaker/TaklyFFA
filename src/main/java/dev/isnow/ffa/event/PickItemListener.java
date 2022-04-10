package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.type.LimitType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickItemListener implements Listener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if(TaklyFFA.INSTANCE.limitTypeArrayList.contains(new LimitType(e.getItem().getItemStack().getType(), e.getItem().getItemStack().getDurability()))) {
            e.setCancelled(true);
        }
    }
}
