package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setHelmet(TaklyFFA.INSTANCE.kitManager.armor.get(0));
        e.getPlayer().getInventory().setChestplate(TaklyFFA.INSTANCE.kitManager.armor.get(1));
        e.getPlayer().getInventory().setLeggings(TaklyFFA.INSTANCE.kitManager.armor.get(2));
        e.getPlayer().getInventory().setBoots(TaklyFFA.INSTANCE.kitManager.armor.get(3));

        for(final ItemStack i : TaklyFFA.INSTANCE.kitManager.items.keySet()) {
            e.getPlayer().getInventory().setItem(TaklyFFA.INSTANCE.kitManager.items.get(i), i);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().teleport(TaklyFFA.INSTANCE.spawnManager.getSpawnLocation().toBukkitLocation());
            }
        }.runTaskLater(TaklyFFA.INSTANCE.getPlugin(), 3);
    }
}
