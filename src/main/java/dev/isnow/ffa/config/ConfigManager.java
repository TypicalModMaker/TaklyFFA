package dev.isnow.ffa.config;

import dev.isnow.ffa.TaklyFFA;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

// This shit was not made by me
// Dont laugh about this shit
public class ConfigManager
{

    public FileConfiguration mysql,scoreboard,spawn,elo,kit,gui;

    public ConfigManager(boolean mysql) {
        if(mysql) {
            this.mysql = copyConfig("mysql.yml");
            return;
        }
        loadConfigs();
    }

    public FileConfiguration copyConfig(String name) {
        TaklyFFA.INSTANCE.getPlugin().saveResource(name, false);
        return YamlConfiguration.loadConfiguration(new File(TaklyFFA.INSTANCE.getPlugin().getDataFolder(), name));
    }

    public void saveConfig(FileConfiguration config, String name) throws IOException {
        File dir = TaklyFFA.INSTANCE.getPlugin().getDataFolder();
        if (!dir.isDirectory()) {
            final boolean result = dir.mkdirs();
        }
        File file = new File(TaklyFFA.INSTANCE.getPlugin().getDataFolder(), name + ".yml");
        config.save(file);
    }

    public FileConfiguration getConfig(String name) {
        File dir = TaklyFFA.INSTANCE.getPlugin().getDataFolder();
        if (!dir.isDirectory()) {
            return null;
        }
        File file = new File(dir, name + ".yml");
        if (!file.exists()) {
            return null;
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration reloadConfig(String name) {
        File dir = TaklyFFA.INSTANCE.getPlugin().getDataFolder();
        if (!dir.isDirectory()) {
            return null;
        }
        FileConfiguration config = getConfig(name);
        try {
            config.load(new File(dir, name + ".yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    public void loadConfigs() {
        scoreboard = copyConfig("scoreboard.yml");
        spawn = copyConfig("spawn.yml");
        elo = copyConfig("elo.yml");
        kit = copyConfig("kit.yml");
        gui = copyConfig("gui.yml");
        mysql = copyConfig("mysql.yml");
    }
}
