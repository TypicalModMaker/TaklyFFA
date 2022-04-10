package dev.isnow.ffa.manager;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.cuboid.Cuboid;
import dev.isnow.ffa.utils.cuboid.CustomLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter @Setter
public class SpawnManager {

    private final TaklyFFA plugin = TaklyFFA.INSTANCE;

    private CustomLocation spawnLocation;
    private CustomLocation safezoneMin;
    private CustomLocation safezoneMax;

    private Cuboid cuboid;

    public SpawnManager() {
        this.loadConfig();
    }

    private void loadConfig() {
        final FileConfiguration config = this.plugin.configManager.spawn;

        if (config.contains("spawn")) {
            try {
                this.spawnLocation = CustomLocation.stringToLocation(config.getString("spawn.location"));
                this.safezoneMin = CustomLocation.stringToLocation(config.getString("spawn.safezone-min"));
                this.safezoneMax = CustomLocation.stringToLocation(config.getString("spawn.safezone-max"));
                this.cuboid = new Cuboid(safezoneMin.toBukkitLocation(), safezoneMax.toBukkitLocation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
