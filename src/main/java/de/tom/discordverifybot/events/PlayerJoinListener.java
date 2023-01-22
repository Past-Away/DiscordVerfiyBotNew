package de.tom.discordverifybot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    private DiscordVerifyBot plugin;

    public PlayerJoinListener(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event ){
        Player player = event.getPlayer();

        if (!plugin.getVerifiedPlayersFile().isVerifiedPlayer(player.getUniqueId().toString())) {
            player.sendTitle(plugin.getConfig().getString("title.firstLine").replace("&", "§"), plugin.getConfig().getString("title.secondLine").replace("&", "§"), 10, 70*60 , 20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 100));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
        }
    }
}
