package de.tom.discordverifybot.commands;

import de.tom.discordverifybot.DiscordVerifyBot;
import de.tom.discordverifybot.events.DiscordEvents;
import de.tom.discordverifybot.mysql.Methods;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class VerifyCommand implements CommandExecutor {

    DiscordVerifyBot plugin;

    MessageReceivedEvent events = DiscordEvents.event;
    public VerifyCommand(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {

        Player player = (Player) sender;

        if (args.length == 1) {
            if (plugin.getVerifyCodes().containsKey(player)) {
                if (args[0].equalsIgnoreCase(plugin.getVerifyCodes().get(player))) {
                    player.sendMessage("§aVErifed");
                    plugin.getVerifyCodes().remove(player);
                    User user = plugin.getJda().retrieveUserById(plugin.getDiscordID().get(player)).complete();
                    plugin.getHikariCP().setVerifiedUser(player, player.getUniqueId(), player.getName(), plugin.getDiscordID().get(player), user.getName());

                    DiscordEvents.createFinishVerifyPrivatEmbed(plugin,user);
                    plugin.getVerifyCodes().remove(player);
                    plugin.getRequestedCode().remove(user);

                    player.removePotionEffect(PotionEffectType.SLOW);
                    player.removePotionEffect(PotionEffectType.BLINDNESS);

                } else {
                    player.sendMessage("§cYou have the wrong code please check the chat again");
                }
            } else {
                player.sendMessage("§cYou do not have a code please verify in discord");
            }

        } else {
            player.sendMessage("§7----- §aDiscordVerify §7-----");
            player.sendMessage("§8- §e/Verify §7<§ccode§7>");
            player.sendMessage("§7----- §aDiscordVerify §7-----");
        }


        return false;
    }
}
