package me.realized.de.placeholders;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import me.realized.de.placeholders.hooks.MVdWPlaceholderHook;
import me.realized.de.placeholders.hooks.PlaceholderHook;
import me.realized.de.placeholders.util.StringUtil;
import me.realized.de.placeholders.util.Updatable;
import me.realized.de.placeholders.util.compat.Ping;
import me.realized.duels.api.Duels;
import me.realized.duels.api.arena.Arena;
import me.realized.duels.api.arena.ArenaManager;
import me.realized.duels.api.event.kit.KitCreateEvent;
import me.realized.duels.api.extension.DuelsExtension;
import me.realized.duels.api.kit.Kit;
import me.realized.duels.api.kit.KitManager;
import me.realized.duels.api.match.Match;
import me.realized.duels.api.user.User;
import me.realized.duels.api.user.UserManager;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Placeholders extends DuelsExtension implements Listener {

    private String userNotFound;
    private String notInMatch;
    private String durationFormat;
    private String noKit;
    private String noOpponent;

    @Getter
    private UserManager userManager;
    @Getter
    private KitManager kitManager;
    @Getter
    private ArenaManager arenaManager;

    private final List<Updatable<Kit>> updatables = new ArrayList<>();

    @Override
    public void onEnable() {
        final FileConfiguration config = getConfig();
        this.userNotFound = config.getString("user-not-found");
        this.notInMatch = config.getString("not-in-match");
        this.durationFormat = config.getString("duration-format");
        this.noKit = config.getString("no-kit");
        this.noOpponent = config.getString("no-opponent");

        this.userManager = api.getUserManager();
        this.kitManager = api.getKitManager();
        this.arenaManager = api.getArenaManager();

        api.registerListener(this);
        doIfFound("PlaceholderAPI", () -> register(PlaceholderHook.class));
        doIfFound("MVdWPlaceholderAPI", () -> register(MVdWPlaceholderHook.class));
    }

    @Override
    public void onDisable() {
        updatables.clear();
    }

    @Override
    public String getRequiredVersion() {
        return "3.1.2";
    }

    private void doIfFound(final String name, final Runnable action) {
        final Plugin plugin = api.getServer().getPluginManager().getPlugin(name);

        if (plugin == null || !plugin.isEnabled()) {
            return;
        }

        action.run();
    }

    @SuppressWarnings("unchecked")
    private void register(final Class<? extends Updatable<Kit>> clazz) {
        try {
            updatables.add(clazz.getConstructor(Placeholders.class, Duels.class).newInstance(this, api));
        } catch (Exception ignored) {}
    }

    public String find(final Player player, String identifier) {
        if (player == null) {
            return "Player is required";
        }

        User user;

        if (identifier.startsWith("rating_")) {
            user = userManager.get(player);

            if (user == null) {
                return StringUtil.color(userNotFound);
            }

            final Kit kit = kitManager.get(identifier.replace("rating_", ""));
            return kit != null ? String.valueOf(user.getRating(kit)) : StringUtil.color(noKit);
        }

        if (identifier.startsWith("match_")) {
            identifier = identifier.replace("match_", "");
            final Arena arena = arenaManager.get(player);

            if (arena == null) {
                return StringUtil.color(notInMatch);
            }

            final Match match = arena.getMatch();

            if (match == null) {
                return StringUtil.color(notInMatch);
            }

            if (identifier.equalsIgnoreCase("duration")) {
                return DurationFormatUtils.formatDuration(System.currentTimeMillis() - match.getStart(), durationFormat);
            }

            if (identifier.equalsIgnoreCase("kit")) {
                return match.getKit() != null ? match.getKit().getName() : StringUtil.color(noKit);
            }

            if (identifier.equalsIgnoreCase("arena")) {
                return match.getArena().getName();
            }

            if (identifier.equalsIgnoreCase("bet")) {
                return String.valueOf(match.getBet());
            }

            if (identifier.equalsIgnoreCase("rating")) {
                user = userManager.get(player);

                if (user == null) {
                    return StringUtil.color(userNotFound);
                }

                return match.getKit() != null ? String.valueOf(user.getRating(match.getKit())) : StringUtil.color(noKit);
            }

            if (identifier.startsWith("opponent")) {
                final Player opponent = match.getPlayers().stream().filter(matchPlayer -> !matchPlayer.equals(player)).findFirst().orElse(null);

                if (opponent == null) {
                    return StringUtil.color(noOpponent);
                }

                if (identifier.equalsIgnoreCase("opponent")) {
                    return opponent.getName();
                }

                if (identifier.endsWith("_health")) {
                    return String.valueOf(Math.ceil(opponent.getHealth()) * 0.5);
                }

                if (identifier.endsWith("_ping")) {
                    return String.valueOf(Ping.getPing(opponent));
                }

                user = userManager.get(opponent);

                if (user == null) {
                    return StringUtil.color(userNotFound);
                }

                return match.getKit() != null ? String.valueOf(user.getRating(match.getKit())) : StringUtil.color(noKit);
            }
        }

        return null;
    }

    @EventHandler
    public void on(final KitCreateEvent event) {
        updatables.forEach(updatable -> updatable.update(event.getKit()));
    }
}
