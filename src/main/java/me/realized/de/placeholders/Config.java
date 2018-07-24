package me.realized.de.placeholders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    @Getter
    private final String userNotFound;
    @Getter
    private final String notInMatch;
    @Getter
    private final String durationFormat;
    @Getter
    private final String noKit;
    @Getter
    private final String noOpponent;

    public Config(final Placeholders extension) {
        final File dataFolder = new File(extension.getFolder(), extension.getName());

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        final File file = new File(dataFolder, "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();

                try (InputStream in = getClass().getResourceAsStream("/config.yml"); OutputStream out = new FileOutputStream(file)) {
                    if (in != null) {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.userNotFound = config.getString("user-not-found");
        this.notInMatch = config.getString("not-in-match");
        this.durationFormat = config.getString("duration-format");
        this.noKit = config.getString("no-kit");
        this.noOpponent = config.getString("no-opponent");
    }
}
