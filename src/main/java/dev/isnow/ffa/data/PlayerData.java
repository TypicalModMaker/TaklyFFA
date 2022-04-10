package dev.isnow.ffa.data;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class PlayerData {

    public int kills,deaths,beststreak,streak,elo,coins;

    public PlayerData(int kills, int deaths, int beststreak, int elo, int coins, int streak) {
        this.kills = kills;
        this.deaths = deaths;
        this.beststreak = beststreak;
        this.elo = elo;
        this.coins = coins;
        this.streak = streak;
    }
}
