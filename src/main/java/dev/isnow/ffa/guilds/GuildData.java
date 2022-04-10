package dev.isnow.ffa.guilds;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;

@Getter@Setter
public class GuildData {
    ArrayList<OfflinePlayer> players;
    int guildElo;
    String tag;
    String name;

    public GuildData(String name, String tag, ArrayList<OfflinePlayer> players, int elo) {
        this.name = name;
        this.tag = tag;
        this.players = players;
        this.guildElo = elo;
    }
}
