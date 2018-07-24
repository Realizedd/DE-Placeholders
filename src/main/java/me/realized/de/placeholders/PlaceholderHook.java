package me.realized.de.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.realized.duels.api.Duels;
import org.bukkit.entity.Player;

public class ClipPlaceholders extends PlaceholderExpansion {

    private final Placeholders extension;
    private final Duels api;

    public ClipPlaceholders(final Placeholders extension, final Duels api) {
        this.extension = extension;
        this.api = api;
        register();
    }

    @Override
    public String getIdentifier() {
        return "duels";
    }

    @Override
    public String getPlugin() {
        return api.getName();
    }

    @Override
    public String getAuthor() {
        return "Realized";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(final Player player, final String s) {
        return extension.find(player, s);
    }
}
