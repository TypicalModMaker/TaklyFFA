package dev.isnow.ffa.commands.admin;

import dev.isnow.ffa.data.PlayerData;
import dev.isnow.ffa.data.PlayerDataManager;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.command.BaseCommand;
import dev.isnow.ffa.utils.command.Command;
import dev.isnow.ffa.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetStatCommand extends BaseCommand {

    @Override @Command(name = "setstat", permission = "takly.admin", aliases = "setstats", usage = "Usage: /setstat <player> <kills/deaths/elo> <amount>")
    public void onCommand(CommandArgs command) {
        final Player player = command.getPlayer();
        final  String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(ColorHelper.translate("&c" + command.getCommand().getUsage()));
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ColorHelper.translate("&cThere is no player named '" + args[0] + "' online."));
            return;
        }

        final PlayerData targetUser = PlayerDataManager.get(target.getUniqueId());
        final int amount = Integer.parseInt(args[2]);
        switch (args[1].toLowerCase()) {
            case "kills":
                targetUser.setKills(amount);
                break;
            case "deaths":
                targetUser.setDeaths(amount);
                break;
            case "elo":
                targetUser.setElo(amount);
                break;
            default:
                player.sendMessage(ColorHelper.translate("&c" + command.getCommand().getUsage()));
                break;
        }

        player.sendMessage(ColorHelper.translate("&aSuccessfully modified " + target.getName() + "'s " + args[1] + " amount to " + amount + "."));
    }
}
