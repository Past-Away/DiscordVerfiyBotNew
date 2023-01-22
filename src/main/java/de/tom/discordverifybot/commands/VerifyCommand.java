package de.tom.discordverifybot.commands;

import de.tom.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Member;

public class VerifyCommand implements CommandExecutor {

    private DiscordVerifyBot plugin;

    public VerifyCommand(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getPrefix() + "§cDu musst ein Spieler sein um diesen Befehl auszuführen!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            if (plugin.getVerifyQueueRepository().isInVerifyQueue(player)) {
                String code = plugin.getVerifyQueueRepository().getVerify(player);
                if (args[0].equalsIgnoreCase(code)) {
                    User user = plugin.getBot().getJda().getUserById(plugin.getVerifyQueueRepository().getVerifySave(player));
                    if (user != null) {
                        plugin.getVerifyQueueRepository().removeVerify(player);
                        plugin.getVerifiedPlayersFile().addVerifiedPlayer(player.getUniqueId().toString(), user.getId());
                        try {
                            plugin.getBot().addRoleToUser(user);
                        } catch (Exception e) {
                            player.sendMessage(plugin.getPrefix() + "§cEs ist ein Fehler aufgetreten! Bitte melde diesen Fehler an einen Admin!");
                        }
                        player.sendMessage(plugin.getConfig().getString("messages.verifySuccessfully").replace("&", "§").replace("%prefix%", plugin.getPrefix()));
                        player.removePotionEffect(PotionEffectType.SLOW);
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        player.resetTitle();
                    }
                } else {
                    player.sendMessage(plugin.getConfig().getString("messages.wrongCode").replace("&", "§").replace("%prefix%", plugin.getPrefix()));
                }
            } else {
                player.sendMessage(plugin.getConfig().getString("messages.isNotInVerifyQueue").replace("&", "§").replace("%prefix%", plugin.getPrefix()));
            }
        } else {
            player.sendMessage(plugin.getConfig().getString("messages.wrongUsage").replace("&", "§").replace("%prefix%", plugin.getPrefix()));
        }

        return false;
    }
}
