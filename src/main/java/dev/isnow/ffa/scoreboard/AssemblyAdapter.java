package dev.isnow.ffa.scoreboard;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.data.PlayerData;
import dev.isnow.ffa.data.PlayerDataManager;
import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.List;

public class AssemblyAdapter implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return TaklyFFA.INSTANCE.configManager.scoreboard.getString("scoreboard-title");
    }

    @Override
    public List<String> getLines(Player player) {
        PlayerData data = PlayerDataManager.get(player.getUniqueId());
        List<String> lines = TaklyFFA.INSTANCE.configManager.scoreboard.getStringList("scoreboard-lines");
        lines.replaceAll(s -> s.replaceAll("%kills%", String.valueOf(data.getKills())).replaceAll("%deaths%", String.valueOf(data.getDeaths())).replaceAll("%coins%", String.valueOf(data.getCoins())).replaceAll("%beststreak%", String.valueOf(data.getBeststreak())).replaceAll("%elo%", String.valueOf(data.getElo())).replaceAll("%streak%", String.valueOf(data.getStreak())));
        return lines;
    }
}
