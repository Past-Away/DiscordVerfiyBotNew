package de.tom.discordverifybot;

import de.tom.discordverifybot.bot.Bot;
import de.tom.discordverifybot.commands.VerifyCommand;
import de.tom.discordverifybot.events.BlockListener;
import de.tom.discordverifybot.events.PlayerDamageListener;
import de.tom.discordverifybot.events.PlayerJoinListener;
import de.tom.discordverifybot.utils.VerifyQueueRepository;
import de.tom.discordverifybot.utils.files.VerifiedPlayersFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class DiscordVerifyBot extends JavaPlugin {

    private static DiscordVerifyBot instance;
    private Bot bot;

    private VerifyQueueRepository verifyQueueRepository;

    private String prefix;

    private VerifiedPlayersFile verifiedPlayersFile;

    //hier die ip einfügen
    private String[] ips = new String[] {
            "185.117.0.214",
    };

    @Override
    public void onEnable() {
        for (String ip : ips) {
            if(Bukkit.getServer().getIp().equals(ip)) {
                instance = this;
                getLogger().log(Level.INFO,"Discord-Verify | Das Plugin wurde erfolgreich geladen!");
                getLogger().log(Level.INFO,"Discord-Verify | Der Bot wird gestartet...");
                onInit();
            } else {
                getLogger().log(Level.INFO,"Discord-Verify | Du besitzt keine Lizenz für dieses Plugin!");
                getLogger().log(Level.INFO,"Discord-Verify | Das Plugin wird nun deaktiviert!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onDisable() {
        bot.shutdown();
    }

    private void onInit() {
        saveDefaultConfig();
        bot = new Bot(getConfig().getString("discord.token"), this);
        getLogger().log(Level.INFO,"Discord-Verify | Der Bot wurde erfolgreich gestartet!");
        prefix = getConfig().getString("messages.prefix").replace("&", "§");
        verifyQueueRepository = new VerifyQueueRepository();
        getCommand("verify").setExecutor(new VerifyCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);
        verifiedPlayersFile = new VerifiedPlayersFile(this);
        getLogger().log(Level.INFO,"Discord-Verify | Das Plugin ist einsatzbereit!");
    }

    public static DiscordVerifyBot getInstance() {
        return instance;
    }

    public Bot getBot() {
        return bot;
    }

    public String getPrefix() {
        return prefix;
    }


    public VerifiedPlayersFile getVerifiedPlayersFile() {
        return verifiedPlayersFile;
    }

    public VerifyQueueRepository getVerifyQueueRepository() {
        return verifyQueueRepository;
    }
}
