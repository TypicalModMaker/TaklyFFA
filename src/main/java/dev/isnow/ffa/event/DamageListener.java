package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player && TaklyFFA.INSTANCE.spawnManager.getCuboid().isIn((Player) e.getEntity())) {
            e.setCancelled(true);
        }
    }
}
