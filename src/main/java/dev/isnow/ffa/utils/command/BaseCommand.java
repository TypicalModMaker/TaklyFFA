package dev.isnow.ffa.utils.command;


import dev.isnow.ffa.TaklyFFA;

public abstract class BaseCommand {

	public BaseCommand() {
		TaklyFFA.INSTANCE.getCommandFramework().registerCommands(this, null);
	}

	public abstract void onCommand(CommandArgs command);
}
