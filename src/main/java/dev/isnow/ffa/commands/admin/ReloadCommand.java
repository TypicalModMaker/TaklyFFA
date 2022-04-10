package dev.isnow.ffa.commands.admin;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.command.BaseCommand;
import dev.isnow.ffa.utils.command.Command;
import dev.isnow.ffa.utils.command.CommandArgs;

public class ReloadCommand extends BaseCommand {

    private final TaklyFFA plugin = TaklyFFA.INSTANCE;

    @Override@Command(name = "reload", aliases = "reloadconfig", permission = "takly.admin")
    public void onCommand(CommandArgs command) {
        plugin.reloadConfigs();
        command.getPlayer().sendMessage(ColorHelper.translate("&aSuccessfully reloaded all the configs. &c&LBETA"));
    }
}
