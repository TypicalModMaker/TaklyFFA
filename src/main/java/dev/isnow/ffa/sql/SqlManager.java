package dev.isnow.ffa.sql;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.data.PlayerData;
import dev.isnow.ffa.data.PlayerDataManager;
import dev.isnow.ffa.guilds.GuildData;
import dev.isnow.ffa.guilds.GuildDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SqlManager {

    private HikariDataSource hikari;

    public String hostname;
    public String port;
    public String database;
    public String username;
    public String password;

    private int minimumConnections;
    private int maximumConnections;
    private long connectionTimeout;

    public SqlManager() {

        init();
        setupPool();
        createTable();
    }

    private void init() {

        final FileConfiguration fileConfig = TaklyFFA.INSTANCE.configManager.mysql;
        this.username = fileConfig.getString("username");
        this.password = fileConfig.getString("password");
        this.database = fileConfig.getString("database");
        this.hostname = fileConfig.getString("ip");
        this.port = fileConfig.getString("port");

        this.minimumConnections = 5;
        this.maximumConnections = 100;
        this.connectionTimeout = 30000;
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" +
                        hostname +
                        ":" +
                        port +
                        "/" +
                        database +
                        "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
        );
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumConnections);
        config.setMaximumPoolSize(maximumConnections);
        config.setConnectionTimeout(connectionTimeout);
        config.setLeakDetectionThreshold(3000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        //config.setConnectionTestQuery(testQuery);
        hikari = new HikariDataSource(config);
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }

    public void closePool() {
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
        }
    }

    public void reload() {
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
        }

        final FileConfiguration fileConfig = TaklyFFA.INSTANCE.configManager.mysql;
        this.username = fileConfig.getString("username");
        this.password = fileConfig.getString("password");
        this.database = fileConfig.getString("database");
        this.hostname = fileConfig.getString("ip");
        this.port = fileConfig.getString("port");
        setupPool();
        createTable();
    }

    public String toBase64(ArrayList<OfflinePlayer> players) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(players);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save players.", e);
        }
    }

    public ArrayList<OfflinePlayer> fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ArrayList<OfflinePlayer> is = (ArrayList<OfflinePlayer>) dataInput.readObject();
            dataInput.close();
            return is;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public void createTable(){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_data_ffa ("
                    + "UUID VARCHAR(150) NOT NULL,"
                    + "Kills Int NOT NULL,"
                    + "Deaths Int NOT NULL,"
                    + "BestStreak Int NOT NULL,"
                    + "Elo Int NOT NULL,"
                    + "Coins Int NOT NULL,"
                    + "PRIMARY KEY (UUID))");
            statement.executeUpdate();
        } catch(SQLException exception) {
            exception.printStackTrace();
        } finally {
            close(connection, statement, null);
        }
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS guild_data_ffa ("
                    + "Name VARCHAR(150) NOT NULL,"
                    + "Tag VARCHAR(150) NOT NULL,"
                    + "Users TEXT NOT NULL,"
                    + "Elo Int NOT NULL,"
                    + "PRIMARY KEY (Name))");
            statement.executeUpdate();
        } catch(SQLException exception) {
            exception.printStackTrace();
        } finally {
            close(connection, statement, null);
        }
    }

    public ArrayList<GuildData> getEveryGuildData() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM guild_data_ffa");
            statement.executeQuery();
            result = statement.getResultSet();
            while (result.next()) {
                Bukkit.getConsoleSender().sendMessage(result.getString("Tag"));
//                ArrayList<OfflinePlayer> users = fromBase64(result.getString("Users"));
//                int elo = result.getInt("Elo");
//                Data.add(tag);
//                Data.add(users);
//                Data.add(elo);
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        } finally {
            close(connection, statement, result);
        }
        return null;
    }

    public GuildData getGuildDataFromName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM guild_data_ffa WHERE Name='" + name + "';");
            statement.executeQuery();
            result = statement.getResultSet();
            ArrayList<Object> data = new ArrayList<>();
            while (result.next()) {
                String tag = result.getString("Tag");
                ArrayList<OfflinePlayer> users = fromBase64(result.getString("Users"));
                int elo = result.getInt("Elo");
                data.add(tag);
                data.add(users);
                data.add(elo);
            }
            return new GuildData((ArrayList<OfflinePlayer>) data.get(1), (int) data.get(2), name, (String) data.get(0));
        } catch(Exception exception) {
            exception.printStackTrace();
        } finally {
            close(connection, statement, result);
        }
        return null;
    }

    public ArrayList<Integer> getPlayerDataFromUUID(UUID uuid) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM player_data_ffa WHERE UUID='" + uuid.toString() + "';");
            statement.executeQuery();
            result = statement.getResultSet();
            ArrayList<Integer> Data = new ArrayList<>();
            while (result.next()) {
                int kills = result.getInt("Kills");
                int deaths = result.getInt("Deaths");
                int beststreak = result.getInt("BestStreak");
                int elo = result.getInt("Elo");
                int coins = result.getInt("Coins");
                Data.add(kills);
                Data.add(deaths);
                Data.add(beststreak);
                Data.add(elo);
                Data.add(coins);
            }
            return Data;
        } catch(Exception exception) {
            exception.printStackTrace();
        } finally {
            close(connection, statement, result);
        }
        return null;
    }


    public void setStuff(UUID uuid) {
        PlayerData playerData = PlayerDataManager.get(uuid);
        if(getPlayerDataFromUUID(uuid).size() == 0) {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = hikari.getConnection();
                statement = connection.prepareStatement("INSERT INTO player_data_ffa (UUID, Kills, Deaths, BestStreak, Elo, Coins)" + " VALUES ('" + uuid.toString() + "','" + playerData.getKills() + "','" + playerData.getDeaths() + "','" + playerData.getBeststreak() + "','" + playerData.getElo() + "','" + playerData.getCoins() + "');");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(connection, statement, null);
            }
        }
        else {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = hikari.getConnection();
                statement = connection.prepareStatement("REPLACE INTO player_data_ffa (UUID, Kills, Deaths, BestStreak, Elo, Coins)" + " VALUES ('" + uuid.toString() + "','" + playerData.getKills() + "','" + playerData.getDeaths() + "','" + playerData.getBeststreak() + "','" + playerData.getElo() + "','" + playerData.getCoins() + "');");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(connection, statement, null);
            }
        }
    }

    public void setStuffGuild(String name) {
        GuildData guildData = GuildDataManager.get(name);
        GuildData datasql = getGuildDataFromName(name);
        Connection connection = null;
        PreparedStatement statement = null;
        if(datasql != null) {
            try {
                connection = hikari.getConnection();
                statement = connection.prepareStatement("INSERT INTO guild_data_ffa (Name, Tag, Users, Elo)" + " VALUES ('" + name + "','" + guildData.getTag() + "','" + toBase64(guildData.getPlayers()) + "','" + guildData.getGuildElo() + "');");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(connection, statement, null);
            }
        }
        else {
            try {
                connection = hikari.getConnection();
                statement = connection.prepareStatement("REPLACE INTO guild_data_ffa (Name, Tag, Users, Elo)" + " VALUES ('" + name + "','" + guildData.getTag() + "','" + toBase64(guildData.getPlayers()) + "','" + guildData.getGuildElo() + "');");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(connection, statement, null);
            }
        }
    }
}
