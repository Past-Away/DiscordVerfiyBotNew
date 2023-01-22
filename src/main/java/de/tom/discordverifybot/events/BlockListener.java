package de.tom.discordverifybot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {

    private DiscordVerifyBot plugin;

    public BlockListener(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getVerifiedPlayersFile().isVerifiedPlayer(player.getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getVerifiedPlayersFile().isVerifiedPlayer(player.getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }
}
