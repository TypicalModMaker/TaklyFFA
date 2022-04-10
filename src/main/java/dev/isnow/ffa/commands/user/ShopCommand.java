package dev.isnow.ffa.commands.user;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.command.BaseCommand;
import dev.isnow.ffa.utils.command.Command;
import dev.isnow.ffa.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class ShopCommand extends BaseCommand {

    @Override @Command(name = "openshop", aliases = "shop", usage = "Usage: /shop")
    public void onCommand(CommandArgs command) {
        final Player player = command.getPlayer();
        final String[] args = command.getArgs();

        if (args.length > 0) {
            player.sendMessage(ColorHelper.translate("&c" + command.getCommand().getUsage()));
            return;
        }
        TaklyFFA.INSTANCE.getGuiManager().openGui(command.getPlayer());
    }
}
