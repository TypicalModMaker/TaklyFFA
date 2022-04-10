package dev.isnow.ffa.commands.admin;

import dev.isnow.ffa.data.PlayerDataManager;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.command.BaseCommand;
import dev.isnow.ffa.utils.command.Command;
import dev.isnow.ffa.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetCoinsCommand extends BaseCommand {

    @Override @Command(name = "setcoins", aliases = "setbalance", permission = "takly.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(ColorHelper.translate("&cUsage: /setcredits <player> <credits>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ColorHelper.translate("&cThere are no players named '" + args[0] + "' online."));
            return;
        }

        PlayerDataManager.get(target.getUniqueId()).setCoins(Integer.parseInt(args[1]));
        player.sendMessage(ColorHelper.translate("&aSuccessfully modified " + target.getName() + "'s coins amount to " + args[1] + "."));
    }
}
