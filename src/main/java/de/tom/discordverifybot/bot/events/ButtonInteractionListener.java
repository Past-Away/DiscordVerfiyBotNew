package de.tom.discordverifybot.bot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;


public class ButtonInteractionListener extends ListenerAdapter {
    private DiscordVerifyBot plugin;

    public ButtonInteractionListener(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("confirm")) {
            if (plugin.getVerifiedPlayersFile().isVerifiedDiscordId(event.getInteraction().getMember().getId())) {
                String uuid = plugin.getVerifiedPlayersFile().getKey(event.getMember().getId());
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                System.out.println(player.getName());
                plugin.getVerifiedPlayersFile().removeVerifiedPlayer(player.getName());
                event.getInteraction().reply(plugin.getConfig().getString("discord.successfullyDisconnect")).setEphemeral(true).queue();
                Role role = event.getGuild().getRoleById(plugin.getConfig().getString("discord.roleId"));
                if (role != null) {
                    event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
                } else {
                    event.reply(plugin.getConfig().getString("discord.roleNotFound")).setEphemeral(true).queue();
                }
            } else {
                event.reply(plugin.getConfig().getString("discord.notVerified")).setEphemeral(true).queue();
            }
        }
    }
}
