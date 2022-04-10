package dev.isnow.ffa.utils;

import org.bukkit.ChatColor;

public class ColorHelper {
	public static String translate(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
