package me.realized.de.placeholders.util;

import org.bukkit.ChatColor;

public final class StringUtil {

    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private StringUtil() {}
}
