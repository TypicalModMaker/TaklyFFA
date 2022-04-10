package dev.isnow.ffa.event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class NoWaterListener implements Listener {

    @EventHandler
    public void blockMove(BlockFromToEvent e) {
        if(e.getBlock().getType() == Material.WATER) {
            e.setCancelled(true);
        }
    }
}
