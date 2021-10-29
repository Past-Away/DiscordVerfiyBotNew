package de.tom.discordverifybot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    DiscordVerifyBot plugin;

    public PlayerQuitEvent(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        if (plugin.getJoinedPlayers().contains(event.getPlayer().getName())) {
            plugin.getJoinedPlayers().remove(event.getPlayer().getName());
        }
    }
}
