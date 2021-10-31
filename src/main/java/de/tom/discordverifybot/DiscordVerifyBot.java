package de.tom.discordverifybot;

import de.tom.discordverifybot.commands.VerifyCommand;
import de.tom.discordverifybot.events.DiscordEvents;
import de.tom.discordverifybot.events.PlayerJoinEvent;
import de.tom.discordverifybot.events.PlayerQuitEvent;
import de.tom.discordverifybot.mysql.HikariCP;
import de.tom.discordverifybot.mysql.Methods;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;

public final class DiscordVerifyBot extends JavaPlugin {

    JDA jda;
    HikariCP hikariCP;
    Methods methods;
    public static DiscordVerifyBot instance;
    public ArrayList<String> JoinedPlayers = new ArrayList<>();
    public ArrayList<User> requestedCode = new ArrayList<>();
    public HashMap<Player, String> VerifyCodes = new HashMap<>();
    public HashMap<Player, String> DiscordID = new HashMap<>();
    public HashMap<Player, String> FirstDiscordID = new HashMap<>();
    @Override
    public void onEnable() {
        instance = this;
        hikariCP = new HikariCP(this);
        startDiscordBot();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(this), this);
        getCommand("verify").setExecutor(new VerifyCommand(this));
    }

    @Override
    public void onDisable() {
        jda.shutdownNow();
        hikariCP.close();
    }

    private void startDiscordBot() {
        try {
            jda = JDABuilder.createDefault("OTAyNjI4MDM3OTIzODQ4Mjgy.YXhL3g.J9GdamEdJuowG-Wn7EEJC7iy2MA").build();
            jda.addEventListener(new DiscordEvents(this));
        }catch (Exception e) {
            System.out.println("[DiscordVerifyBot] The provided token is invalid!");
        }
    }


    public JDA getJda() {
        return jda;
    }

    public static DiscordVerifyBot getInstance() {
        return instance;
    }

    public ArrayList<String> getJoinedPlayers() {
        return JoinedPlayers;
    }

    public Methods getMethods() {
        return methods;
    }

    public HikariCP getHikariCP() {
        return hikariCP;
    }

    public HashMap<Player, String> getVerifyCodes() {
        return VerifyCodes;
    }

    public HashMap<Player, String> getDiscordID() {
        return DiscordID;
    }

    public ArrayList<User> getRequestedCode() {
        return requestedCode;
    }

    public HashMap<Player, String> getFirstDiscordID() {
        return FirstDiscordID;
    }
}
