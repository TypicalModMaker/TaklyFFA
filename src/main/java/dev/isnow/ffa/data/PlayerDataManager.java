package dev.isnow.ffa.data;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.UUID;

@UtilityClass
public class PlayerDataManager {

    private final HashMap<UUID, PlayerData> data = new HashMap<>();

    public PlayerData get(UUID uuid) {
        return data.get(uuid);
    }

    public void set(UUID uuid, PlayerData data) {
        PlayerDataManager.data.put(uuid, data);
    }
}
