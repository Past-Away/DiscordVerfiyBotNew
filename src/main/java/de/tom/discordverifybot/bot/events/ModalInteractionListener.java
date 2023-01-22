package de.tom.discordverifybot.bot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class ModalInteractionListener extends ListenerAdapter {

    private DiscordVerifyBot plugin;

    public ModalInteractionListener(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equalsIgnoreCase("verify-modal")) {
            String title = event.getValue("verify-title").getAsString();
            Player player = Bukkit.getPlayer(title);
            if (player != null) {
                if (!plugin.getVerifiedPlayersFile().isVerifiedPlayer(player.getUniqueId().toString()) && !plugin.getVerifiedPlayersFile().isVerifiedDiscordId(event.getUser().getId())) {
                    if (!plugin.getVerifyQueueRepository().isInVerifyQueue(player)) {
                        plugin.getVerifyQueueRepository().addVerify(player, generate(12));
                        plugin.getVerifyQueueRepository().addVerifySave(player, event.getMember().getId());
                        event.reply(plugin.getConfig().getString("discord.codeMessage").replace("%code%", plugin.getVerifyQueueRepository().getVerify(player))).setEphemeral(true).queue();
                    } else {
                        event.reply(plugin.getConfig().getString("discord.alreadyInQueue").replace("%prefix%", plugin.getPrefix())).setEphemeral(true).queue();
                    }
                } else {
                    event.reply(plugin.getConfig().getString("discord.alreadyVerified").replace("%prefix%", plugin.getPrefix())).setEphemeral(true).queue();
                }
            } else {
                event.reply(plugin.getConfig().getString("discord.userNotOnServer").replace("%prefix%", plugin.getPrefix())).setEphemeral(true).queue();
            }
        } else if (event.getModalId().equalsIgnoreCase("getVerify-modal")) {
            String title = event.getValue("getVerify-title").getAsString();
            Player player = Bukkit.getPlayer(title);
            if (player != null) {
                if(plugin.getVerifiedPlayersFile().getKey(event.getInteraction().getMember().getId()) != null) {
                    event.replyEmbeds(sendUserEmbed(player, event.getInteraction().getMember().getUser()).build()).setEphemeral(true).queue();
                } else {
                    event.reply(plugin.getConfig().getString("discord.notVerified").replace("%prefix%", plugin.getPrefix())).setEphemeral(true).queue();
                }
            } else {
                event.reply(plugin.getConfig().getString("discord.userNotOnServer").replace("%prefix%", plugin.getPrefix())).setEphemeral(true).queue();
            }
        }
    }

    private EmbedBuilder sendUserEmbed(Player player, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Verified-User");
        embedBuilder.setDescription("Du hast hier eine Übersicht über die Daten von");
        embedBuilder.addField("Minecraft-Name", player.getName(), true);
        embedBuilder.addField("Minecraft-UUID", player.getUniqueId().toString(), true);
        embedBuilder.addField("Discord-Name", user.getName(), true);
        embedBuilder.addField("Discord-ID", user.getId(), true);
        embedBuilder.setThumbnail("http://cravatar.eu/helmhead/"+player.getName()+"/600.png");
        return embedBuilder;
    }

    private final String[] charCategories = new String[]{
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789"
    };

    private String generate(int length) {
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < length; i++) {
            String charCategory = charCategories[random.nextInt(charCategories.length)];
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }

        return new String(password);
    }


}
