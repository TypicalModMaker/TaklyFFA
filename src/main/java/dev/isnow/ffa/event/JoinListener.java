package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.data.PlayerData;
import dev.isnow.ffa.data.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID plrUUID = e.getPlayer().getUniqueId();

        ArrayList<Integer> data = TaklyFFA.INSTANCE.sqlManager.getPlayerDataFromUUID(plrUUID);
        if(data.size() == 0) {
            PlayerDataManager.set(plrUUID, new PlayerData(0, 0, 0, TaklyFFA.INSTANCE.configManager.elo.getInt("elo-start"), 0, 0));
        }
        else {
            PlayerDataManager.set(plrUUID, new PlayerData(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), 0));
        }

        if(TaklyFFA.INSTANCE.spawnManager.getSpawnLocation() != null) {
            e.getPlayer().teleport(TaklyFFA.INSTANCE.spawnManager.getSpawnLocation().toBukkitLocation());
        }

        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setHelmet(TaklyFFA.INSTANCE.kitManager.armor.get(0));
        e.getPlayer().getInventory().setChestplate(TaklyFFA.INSTANCE.kitManager.armor.get(1));
        e.getPlayer().getInventory().setLeggings(TaklyFFA.INSTANCE.kitManager.armor.get(2));
        e.getPlayer().getInventory().setBoots(TaklyFFA.INSTANCE.kitManager.armor.get(3));

        for(ItemStack i : TaklyFFA.INSTANCE.kitManager.items.keySet()) {
            e.getPlayer().getInventory().setItem(TaklyFFA.INSTANCE.kitManager.items.get(i), i);
        }
    }
}
