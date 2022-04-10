package dev.isnow.ffa.guilds;

import lombok.experimental.UtilityClass;

import java.util.HashMap;

@UtilityClass
public class GuildDataManager {

    private final HashMap<String, GuildData> data = new HashMap<>();

    public GuildData get(String name) {
        return data.get(name);
    }

    public void set(String name, GuildData data) {
       GuildDataManager.data.put(name, data);
    }
}
