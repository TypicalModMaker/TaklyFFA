package dev.isnow.ffa.guilds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;

@Getter@Setter@AllArgsConstructor
public class GuildData {
    ArrayList<OfflinePlayer> players;
    int guildElo;
    String tag;
    String name;
}
