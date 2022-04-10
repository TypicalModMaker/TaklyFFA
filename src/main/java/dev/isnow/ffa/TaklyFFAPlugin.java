package dev.isnow.ffa;

import org.bukkit.plugin.java.JavaPlugin;

public final class TaklyFFAPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        TaklyFFA.INSTANCE.init(this);
    }

    @Override
    public void onDisable() {
        TaklyFFA.INSTANCE.stop();
    }

}
