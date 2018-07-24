package me.realized.de.placeholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.realized.duels.api.Duels;
import me.realized.duels.api.kit.Kit;

public class MVdWPlaceholders implements PlaceholderReplacer, Updatable<Kit> {

    private final Placeholders extension;
    private final Duels api;

    public MVdWPlaceholders(final Placeholders extension, final Duels api) {
        this.extension = extension;
        this.api = api;
        PlaceholderAPI.registerPlaceholder(api, "duels_match_duration", this);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_kit", this);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_arena", this);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_bet", this);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_rating", this);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_opponent", this);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_opponent_rating", this);
        extension.getKitManager().getKits().forEach(kit -> PlaceholderAPI.registerPlaceholder(api, "duels_rating_" + kit.getName().replace(" ", "-"), this));
    }

    @Override
    public String onPlaceholderReplace(final PlaceholderReplaceEvent event) {
        return extension.find(event.getPlayer(), event.getPlaceholder().replace("duels_", ""));
    }

    @Override
    public void update(final Kit kit) {
        PlaceholderAPI.registerPlaceholder(api, "duels_rating_" + kit.getName().replace(" ", "-"), this);
    }
}
