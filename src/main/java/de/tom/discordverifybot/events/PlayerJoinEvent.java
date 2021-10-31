package de.tom.discordverifybot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinEvent implements Listener {

    DiscordVerifyBot plugin;



    public PlayerJoinEvent(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!(plugin.getJoinedPlayers().contains(event.getPlayer().getName()))) {
            plugin.getJoinedPlayers().add(event.getPlayer().getName());
            System.out.println("Array: " + plugin.getJoinedPlayers());
        }

        if (!plugin.getHikariCP().isPlayerInListMcUUID(player.getUniqueId().toString())) {
           player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2000000,100));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2000000,100));
            player.sendTitle("§aPlease verify yourself in the Discord", "§7/verify §7<code>", 20, 2000000, 20);
        }
    }
}
