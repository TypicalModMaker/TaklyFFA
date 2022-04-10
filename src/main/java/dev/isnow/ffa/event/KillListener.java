package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.data.PlayerData;
import dev.isnow.ffa.data.PlayerDataManager;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.ItemBuilder;
import dev.isnow.ffa.utils.type.IntegerRange;
import dev.isnow.ffa.utils.type.KillType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class KillListener implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            e.getDrops().clear();

            Player killer = e.getEntity().getKiller();

            for(KillType kt : TaklyFFA.INSTANCE.killTypeArrayList) {
                if(TaklyFFA.INSTANCE.getPlugin().getConfig().getBoolean("kill-rewards-to-inventory")) {
                    killer.getInventory().addItem(new ItemStack(kt.getMaterial(), kt.getAmount(), kt.getData()));
                }
                else {
                    e.getDrops().add(new ItemStack(kt.getMaterial(), kt.getAmount(), kt.getData()));
                }
            }

            PlayerData killerData = PlayerDataManager.get(killer.getUniqueId());
            PlayerData playerData = PlayerDataManager.get(e.getEntity().getUniqueId());

            playerData.setStreak(0);
            killerData.setStreak(killerData.getStreak() + 1);
            if(killerData.getStreak() > killerData.getBeststreak()) {
                killerData.setBeststreak(killerData.getStreak());
            }

            playerData.setDeaths(playerData.getDeaths() + 1);
            killerData.setKills(killerData.getKills() + 1);

            killerData.setCoins(killerData.getCoins() + 10);
            int[] eloChanges = getEloValues(playerData.getElo(), killerData.getElo());

            if(playerData.getElo() - eloChanges[1] > 0) {
                playerData.setElo(playerData.getElo() - eloChanges[1]);
            }
            else {
                playerData.setElo(0);
            }
            killerData.setElo(killerData.getElo() + eloChanges[0]);

            if(TaklyFFA.INSTANCE.killStreakHashmap.get(killerData.getStreak()) != null) {
                KillType type = TaklyFFA.INSTANCE.killStreakHashmap.get(killerData.getStreak());
                ItemStack is = new ItemBuilder(new ItemStack(type.getMaterial(), type.getAmount(), type.getData())).build();
                killer.getInventory().addItem(is);
                killer.sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getString("killstreak-message")));
            }

            if(TaklyFFA.INSTANCE.getPlugin().getConfig().getBoolean("kill-lightning")) {
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
            }

            if(TaklyFFA.INSTANCE.getPlugin().getConfig().getBoolean("regenerate-armor")) {
                for(ItemStack i : killer.getInventory().getContents()) {
                    if(i != null && i.getType() != null && i.getType() != Material.AIR) {
                        if(i.getType() == TaklyFFA.INSTANCE.kitManager.armor.get(0).getType()) {
                            i.setDurability(TaklyFFA.INSTANCE.kitManager.armor.get(0).getType().getMaxDurability());
                        }
                        else if(i.getType() == TaklyFFA.INSTANCE.kitManager.armor.get(1).getType()) {
                            i.setDurability(TaklyFFA.INSTANCE.kitManager.armor.get(1).getType().getMaxDurability());
                        }
                        else if(i.getType() == TaklyFFA.INSTANCE.kitManager.armor.get(2).getType()) {
                            i.setDurability(TaklyFFA.INSTANCE.kitManager.armor.get(2).getType().getMaxDurability());
                        }
                        else if(i.getType() == TaklyFFA.INSTANCE.kitManager.armor.get(3).getType()) {
                            i.setDurability(TaklyFFA.INSTANCE.kitManager.armor.get(3).getType().getMaxDurability());
                        }
                    }
                }
            }

            if(!TaklyFFA.INSTANCE.getPlugin().getConfig().getBoolean("global-chat-message-only")) {
                killer.sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getConfigurationSection("elo-messages").getString("killer").replaceAll("%victim%", e.getEntity().getName()).replaceAll("%elo%", String.valueOf(eloChanges[0]))));
                e.getEntity().sendMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getConfigurationSection("elo-messages").getString("victim").replaceAll("%attacker%", killer.getName()).replaceAll("%elo%", String.valueOf(eloChanges[1]))));
            }

            e.setDeathMessage(ColorHelper.translate(TaklyFFA.INSTANCE.getPlugin().getConfig().getConfigurationSection("messages").getString("killed-message").replaceAll("%victim%", e.getEntity().getName()).replaceAll("%attacker%", killer.getName()).replaceAll("%victimelo%", String.valueOf(eloChanges[1])).replaceAll("%attackerelo%", String.valueOf(eloChanges[0]))));

        }
        else {
            if(TaklyFFA.INSTANCE.getPlugin().getConfig().getBoolean("kill-lightning")) {
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
            }
            e.getDrops().clear();
            for(KillType kt : TaklyFFA.INSTANCE.killTypeArrayList) {
                e.getDrops().add(new ItemStack(kt.getMaterial(), kt.getAmount(), kt.getData()));
            }
        }
    }

    private int[] getEloValues(final float victimPoints, final float attackerPoints) {
        final int[] rankChanges = new int[2];
        HashMap<IntegerRange, Integer> map = new HashMap<>();

        for(String s : TaklyFFA.INSTANCE.configManager.elo.getStringList("elo-rank-changes")) {
            String[] splitted = s.split("-");
            if(splitted[1].equals("inf")) {
                splitted[1] = String.valueOf(Integer.MAX_VALUE);
            }

            map.put(new IntegerRange(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1])), Integer.parseInt(splitted[2]));
        }

        final int attackerElo = IntegerRange.inRange((int) attackerPoints, map).orElseGet(0);
        final int victimElo = IntegerRange.inRange((int) victimPoints, map).orElseGet(0);

        rankChanges[0] = Math.round(victimPoints / attackerElo);
        rankChanges[1] = Math.round(attackerPoints / victimElo);

        return rankChanges;
    }
}
