package de.tom.discordverifybot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private DiscordVerifyBot plugin;

    public PlayerDamageListener(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!plugin.getVerifiedPlayersFile().isVerifiedPlayer(player.getUniqueId().toString())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!plugin.getVerifiedPlayersFile().isVerifiedPlayer(player.getUniqueId().toString())) {
                event.setCancelled(true);
            }
        }
    }

}
