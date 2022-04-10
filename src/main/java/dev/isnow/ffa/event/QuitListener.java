package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TaklyFFA.INSTANCE.sqlManager.setStuff(event.getPlayer().getUniqueId());
    }
}
