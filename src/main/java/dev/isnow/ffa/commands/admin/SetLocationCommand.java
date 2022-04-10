package dev.isnow.ffa.commands.admin;

import dev.isnow.ffa.TaklyFFA;
import dev.isnow.ffa.utils.ColorHelper;
import dev.isnow.ffa.utils.command.BaseCommand;
import dev.isnow.ffa.utils.command.Command;
import dev.isnow.ffa.utils.command.CommandArgs;
import dev.isnow.ffa.utils.cuboid.CustomLocation;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetLocationCommand extends BaseCommand {

    private final TaklyFFA plugin = TaklyFFA.INSTANCE;

    @Override @Command(name = "setlocation", permission = "takly.admin", usage = "&cUsage: /setlocation <spawn|min|max>")
    public void onCommand(CommandArgs command) {
        final Player player = command.getPlayer();
        final String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ColorHelper.translate(command.getCommand().getUsage()));
            return;
        }

        switch (args[0]) {
            case "spawn":
                this.plugin.spawnManager.setSpawnLocation(CustomLocation.fromBukkitLocation(player.getLocation()));
                saveLocation(player, "spawn.location");
                player.sendMessage(ColorHelper.translate("&aSuccessfully saved spawn location."));
                break;
            case "max":
                this.plugin.spawnManager.setSafezoneMax(CustomLocation.fromBukkitLocation(player.getLocation()));
                saveLocation(player, "spawn.safezone-max");
                player.sendMessage(ColorHelper.translate("&aSuccessfully saved safezone max location."));
                break;
            case "min":
                this.plugin.spawnManager.setSafezoneMin(CustomLocation.fromBukkitLocation(player.getLocation()));
                saveLocation(player, "spawn.safezone-min");
                player.sendMessage(ColorHelper.translate("&aSuccessfully saved safezone min location."));
                break;
            default:
                player.sendMessage(ColorHelper.translate(command.getCommand().getUsage()));
                break;
        }
    }

    private void saveLocation(Player player, String location) {
        this.plugin.configManager.spawn.set(location, CustomLocation.locationToString(CustomLocation.fromBukkitLocation(player.getLocation())));
        try {
            this.plugin.configManager.saveConfig(this.plugin.configManager.spawn, "spawn");
        } catch (IOException e) {
            player.sendMessage(ColorHelper.translate("&cCould not save the config file!"));
            e.printStackTrace();
        }
    }
}
