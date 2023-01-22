package de.tom.discordverifybot.utils.files;

import de.tom.discordverifybot.DiscordVerifyBot;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class VerifiedPlayersFile {

    private static File file;
    private static YamlConfiguration cfg;

    private DiscordVerifyBot plugin;

    public VerifiedPlayersFile(DiscordVerifyBot plugin) {
        this.plugin = plugin;
        try {
            file = new File(plugin.getDataFolder(), "verifiedPlayers.yml");
            if (file.createNewFile()) {
                plugin.getLogger().log(Level.INFO, "Discord-Verify | Die Datei verifiedPlayers.yml wurde erfolgreich erstellt!");
                cfg = YamlConfiguration.loadConfiguration(file);
                load();
            } else {
                plugin.getLogger().log(Level.INFO, "Discord-Verify | Die Datei verifiedPlayers.yml wurde erfolgreich geladen!");
            }
            cfg = YamlConfiguration.loadConfiguration(file);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        cfg.createSection("verifiedPlayers");
        save();
    }

    public void addVerifiedPlayer(String uuid, String discordId) {
        cfg.set("verifiedPlayers." + uuid, discordId);
        save();
    }

    public void removeVerifiedPlayer(String uuid) {
        cfg.set("verifiedPlayers." + uuid, null);
        save();
    }

    public String getVerifiedPlayer(String uuid) {
        return cfg.getString("verifiedPlayers." + uuid);
    }

    public String getKey(String discordId) {
        for (String key : cfg.getConfigurationSection("verifiedPlayers").getKeys(false)) {
            if (cfg.getString("verifiedPlayers." + key).equals(discordId)) {
                return key;
            }
        }
        return null;
    }

    public boolean isVerifiedPlayer(String uuid) {
        return cfg.contains("verifiedPlayers." + uuid);
    }

    public boolean isVerifiedDiscordId(String discordId) {
        for (String key : cfg.getConfigurationSection("verifiedPlayers").getKeys(false)) {
            if (cfg.getString("verifiedPlayers." + key).equals(discordId)) {
                return true;
            }
        }
        return false;
    }

    private void save() {
        try {
            cfg.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
