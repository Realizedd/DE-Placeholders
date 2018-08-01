package me.realized.de.placeholders.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.realized.de.placeholders.Placeholders;
import me.realized.de.placeholders.util.Updatable;
import me.realized.duels.api.Duels;
import me.realized.duels.api.kit.Kit;

public class MVdWPlaceholderHook implements Updatable<Kit> {

    private final Placeholders extension;
    private final Duels api;
    private final PlaceholdersReplacer replacer;

    public MVdWPlaceholderHook(final Placeholders extension, final Duels api) {
        this.extension = extension;
        this.api = api;
        this.replacer = new PlaceholdersReplacer();
        PlaceholderAPI.registerPlaceholder(api, "duels_match_duration", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_kit", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_arena", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_bet", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_rating", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_opponent", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_opponent_health", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_opponent_ping", replacer);
        PlaceholderAPI.registerPlaceholder(api, "duels_match_opponent_rating", replacer);
        extension.getKitManager().getKits().forEach(kit -> PlaceholderAPI.registerPlaceholder(api, "duels_rating_" + kit.getName().replace(" ", "-"), replacer));
    }

    @Override
    public void update(final Kit kit) {
        PlaceholderAPI.registerPlaceholder(api, "duels_rating_" + kit.getName().replace(" ", "-"), replacer);
    }

    public class PlaceholdersReplacer implements PlaceholderReplacer {

        @Override
        public String onPlaceholderReplace(final PlaceholderReplaceEvent event) {
            return extension.find(event.getPlayer(), event.getPlaceholder().replace("duels_", ""));
        }
    }
}
