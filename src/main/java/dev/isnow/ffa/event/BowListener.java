package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.ColorHelper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class BowListener implements Listener {

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && e.getEntity() instanceof Player)
        {
            final Projectile proj = (Projectile)e.getDamager();
            final LivingEntity shooter = (LivingEntity) proj.getShooter();
            final LivingEntity ent = (LivingEntity) e.getEntity();
            shooter.sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getString("hit-projectile-message").replaceAll("%hp%", String.valueOf(ent.getHealth() / 2))));
        }
    }
}
