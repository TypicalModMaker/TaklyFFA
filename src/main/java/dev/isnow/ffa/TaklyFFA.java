package dev.isnow.ffa;

import dev.isnow.ffa.config.ConfigManager;
import dev.isnow.ffa.gui.GuiManager;
import dev.isnow.ffa.guilds.GuildData;
import dev.isnow.ffa.guilds.GuildDataManager;
import dev.isnow.ffa.manager.ClassRegistrationManager;
import dev.isnow.ffa.manager.KitManager;
import dev.isnow.ffa.manager.SpawnManager;
import dev.isnow.ffa.scoreboard.AssemblyAdapter;
import dev.isnow.ffa.sql.SqlManager;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.command.CommandFramework;
import dev.isnow.ffa.utils.type.KillType;
import dev.isnow.ffa.utils.type.LimitType;
import io.github.thatkawaiisam.assemble.Assemble;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public enum TaklyFFA {

    INSTANCE;

    public SqlManager sqlManager;
    public ConfigManager configManager;
    public SpawnManager spawnManager;
    public KitManager kitManager;
    public GuiManager guiManager;

    public ArrayList<LimitType> limitTypeArrayList = new ArrayList<>();
    public ArrayList<KillType> killTypeArrayList = new ArrayList<>();
    public HashMap<Integer, KillType> killStreakHashmap = new HashMap<>();

    private final CommandFramework commandFramework = new CommandFramework(this);
    private final ClassRegistrationManager crc = new ClassRegistrationManager();

    private TaklyFFAPlugin plugin;

    public void init(TaklyFFAPlugin provided) {
        plugin = provided;

        long rn = System.currentTimeMillis();

        plugin.saveDefaultConfig();

        initSQLConfig();
        if(plugin.getConfig().getBoolean("firstrun")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "FIRST RUN DETECTED, EDIT mysql.yml!");
            getPlugin().getConfig().set("firstrun", false);
            getPlugin().saveConfig();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        Bukkit.getConsoleSender().sendMessage("___________       __   .__         ___________________________   ");
        Bukkit.getConsoleSender().sendMessage("\\__    ___/____  |  | _|  | ___.__.\\_   _____/\\_   _____/  _  \\  ");
        Bukkit.getConsoleSender().sendMessage("  |    |  \\__  \\ |  |/ /  |<   |  | |    __)   |    __)/  /_\\  \\ ");
        Bukkit.getConsoleSender().sendMessage("  |    |   / __ \\|    <|  |_\\___  | |     \\    |     \\/    |    \\");
        Bukkit.getConsoleSender().sendMessage("  |____|  (____  /__|_ \\____/ ____| \\___  /    \\___  /\\____|__  /");
        Bukkit.getConsoleSender().sendMessage("               \\/     \\/    \\/          \\/         \\/         \\/ ");
        Bukkit.getConsoleSender().sendMessage("----------------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("© 2022 Isnow - All Rights Reserved.");
        Bukkit.getConsoleSender().sendMessage("Huge Thanks to ELB1TO for commands/cuboid/classregisterator.");
        Bukkit.getConsoleSender().sendMessage("----------------------------------------------------------------------");
        String v = Bukkit.getServer().getClass().getPackage().getName();
        Bukkit.getConsoleSender().sendMessage("Bukkit: " + Bukkit.getName() + " Version " + Bukkit.getVersion());
        Bukkit.getConsoleSender().sendMessage("Using NMS " + v.substring(v.lastIndexOf('.') + 1));
        Bukkit.getConsoleSender().sendMessage("----------------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("Registering listeners...");
        crc.loadListeners("dev.isnow.ffa.event");

        Bukkit.getConsoleSender().sendMessage("Registering commands...");
        crc.loadCommands("dev.isnow.ffa.commands");
        Bukkit.getConsoleSender().sendMessage("Initializing config...");
        initConfig();

        Bukkit.getConsoleSender().sendMessage("Initializing database...");
        initDatabase();

        Bukkit.getConsoleSender().sendMessage("Initializing guilds...");
        initGuilds();

        Bukkit.getConsoleSender().sendMessage("Initializing scoreboard...");
        Assemble assemble = new Assemble(getPlugin(), new AssemblyAdapter());
        assemble.setTicks(2);

        Bukkit.getConsoleSender().sendMessage("Initializing limits...");
        for(String s : TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("item-limit").getKeys(false)) {
            String[] split = s.split("-");
            Material mate = Material.getMaterial(split[0]);
            short meta = 0;
            if(split.length == 2) {
                meta = Short.parseShort(split[1]);
            }

            limitTypeArrayList.add(new LimitType(mate, meta));
        }

        Bukkit.getConsoleSender().sendMessage("Initializing killstreak rewards...");
        for(String s : TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("killstreak-rewards").getKeys(false)) {
            List<String> list = TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("killstreak-rewards").getStringList(s);
            for(String s1 : list) {
                String[] split = s1.split("-");
                Material mate = Material.getMaterial(split[0]);
                int amount = Integer.parseInt(split[1]);
                short meta = 0;
                if(split.length == 3) {
                    meta = Short.parseShort(split[2]);
                }

                killStreakHashmap.put(Integer.parseInt(s), new KillType(mate, meta, amount));
            }
        }

        Bukkit.getConsoleSender().sendMessage("Initializing kill rewards...");
        List<String> list = TaklyFFA.INSTANCE.getPlugin().getConfig().getStringList("kill-rewards");
        for(String s1 : list) {
            String[] split = s1.split("-");
            Material mate = Material.getMaterial(split[0]);
            int amount = Integer.parseInt(split[1]);
            short meta = 0;
            if(split.length == 3) {
                meta = Short.parseShort(split[2]);
            }

            killTypeArrayList.add(new KillType(mate, meta, amount));
        }

        Bukkit.getConsoleSender().sendMessage("Initializing spawn manager...");
        this.spawnManager = new SpawnManager();

        Bukkit.getConsoleSender().sendMessage("Initializing kit manager...");
        this.kitManager = new KitManager();

        Bukkit.getConsoleSender().sendMessage("Initializing GUI manager...");
        this.guiManager = new GuiManager();

        Bukkit.getConsoleSender().sendMessage("----------------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(ColorHelper.translate("&aSuccessfully loaded FFA in " + (System.currentTimeMillis() - rn) + " milliseconds!"));
        Bukkit.getConsoleSender().sendMessage("----------------------------------------------------------------------");
    }

    public void stop() {
        Bukkit.getConsoleSender().sendMessage("___________       __   .__         ___________________________   ");
        Bukkit.getConsoleSender().sendMessage("\\__    ___/____  |  | _|  | ___.__.\\_   _____/\\_   _____/  _  \\  ");
        Bukkit.getConsoleSender().sendMessage("  |    |  \\__  \\ |  |/ /  |<   |  | |    __)   |    __)/  /_\\  \\ ");
        Bukkit.getConsoleSender().sendMessage("  |    |   / __ \\|    <|  |_\\___  | |     \\    |     \\/    |    \\");
        Bukkit.getConsoleSender().sendMessage("  |____|  (____  /__|_ \\____/ ____| \\___  /    \\___  /\\____|__  /");
        Bukkit.getConsoleSender().sendMessage("               \\/     \\/    \\/          \\/         \\/         \\/ ");
        Bukkit.getConsoleSender().sendMessage("Goodbye :)");
        if(sqlManager != null) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                sqlManager.setStuff(p.getUniqueId());
            }
            sqlManager.closePool();
        }
    }

    private void initDatabase() {
        sqlManager = new SqlManager();
    }

    public void initConfig() {
        configManager = new ConfigManager(false);
    }

    public void initSQLConfig() {
        new ConfigManager(true);
    }

    public void initGuilds() {
        GuildDataManager.set("SusPlayers123", new GuildData("SusPlayers123", "SUS123", new ArrayList<>(), 0));
        sqlManager.setStuffGuild("SusPlayers123");
        sqlManager.getEveryGuildData();
    }
    public void reloadConfigs() {
        getPlugin().reloadConfig();
        configManager = new ConfigManager(true);
        spawnManager = new SpawnManager();
        sqlManager.reload();
    }

}