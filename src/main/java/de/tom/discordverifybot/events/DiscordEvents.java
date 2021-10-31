package de.tom.discordverifybot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

public class DiscordEvents extends ListenerAdapter {

    DiscordVerifyBot plugin;
    public static MessageReceivedEvent event;

    public DiscordEvents(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        this.event = event;
        if (event.getMessage().getChannel().getId().equalsIgnoreCase("902630431764119602")) {
            System.out.println(event.getMessage().getContentDisplay());

            if (plugin.getJoinedPlayers().contains(event.getMessage().getContentDisplay())) {
                System.out.println("on server");
                if (!plugin.getHikariCP().isPlayerInList(event.getAuthor().getId())) {
                    System.out.println("not in DAtabase");
                    Player player = Bukkit.getPlayer(event.getMessage().getContentDisplay().toLowerCase());
                    if (!plugin.getRequestedCode().contains(event.getAuthor())) {
                        plugin.getRequestedCode().add(event.getAuthor());
                        String password = generateRandomPassword(8);
                        createVerifyPrivatEmbed(event, password);
                        plugin.getVerifyCodes().put(player, password);
                        plugin.getDiscordID().put(player, event.getAuthor().getId());
                        createEmbed(
                                "MC-Verification",
                                null,
                                Color.green,
                                "The bot has created a code for you, have a look at the private message",
                                "T-Verification",
                                null,
                                null,
                                "T-Verification",
                                null);
                    } else {
                        createEmbed(
                                "MC-Verification",
                                null,
                                Color.red,
                                "You have already requested a code.",
                                "T-Verification",
                                null,
                                null,
                                "T-Verification",
                                null);
                    }

                } else {
                    System.out.println(plugin.getHikariCP().isPlayerInList(event.getAuthor().getId()));
                    System.out.println("allredy in DB");
                    createEmbed(
                            "MC-Verification",
                            null,
                            Color.red,
                            "Your DiscordID is already in use, write to a team member.",
                            "T-Verification",
                            null,
                            null,
                            "T-Verification",
                            null);
                }
            } else if (!plugin.getJoinedPlayers().contains(event.getMessage().getContentDisplay())){
                    createEmbed(
                            "MC-Verification",
                            null,
                            Color.red,
                            "This Player is not Online or Doesn't exists",
                            "T-Verification",
                            null,
                            null,
                            "T-Verification",
                            null);
                System.out.println("Player Not Online");

            }
        }
    }


    public void createVerifyPrivatEmbed(MessageReceivedEvent event, String password) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("MC-Verification", null);
        eb.setColor(Color.red);
        eb.setColor(new Color(0x0C8FF4));
        eb.setColor(new Color(0, 166, 255));

        eb.setDescription("Thank you for the verification, if you have any bugs or need help please contact the server team.\n" +
                "\n" +
                "Your code: **" + password +
                "**\n" +
                "\nUse **/Verify " + password + "** to verify your Discord account");

        eb.setAuthor("T-Verification", null, null);

        eb.setFooter("T-Verification", null);

        event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(eb.build()).queue(), t -> {
            event.getPrivateChannel().sendMessage("Failed").queue();
        });

    }

    public void createEmbed(@Nullable String title, @Nullable String url, Color color, String desc, String author, @Nullable String authorUrl, @Nullable String iconUrl, String footer, @Nullable String iconUrlFooter) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(title, url);
        eb.setColor(color);

        eb.setDescription(desc);

        eb.setAuthor(author, authorUrl, iconUrl);

        eb.setFooter(footer, iconUrlFooter);

        event.getChannel().sendMessage(eb.build()).submit();

    }

    public static void createFinishVerifyPrivatEmbed(DiscordVerifyBot plugin, User user) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("MC-Verification", null);
        eb.setColor(Color.red);
        eb.setColor(new Color(0x14F40C));
        eb.setColor(new Color(0, 255, 4));

        eb.setDescription("You are now done with everything, thank you and have fun :)");

        eb.setAuthor("T-Verification", null, null);

        eb.setFooter("T-Verification", null);

        plugin.getJda().openPrivateChannelById(user.getId()).queue(pc -> pc.sendMessage(eb.build()).queue(), t -> {
            event.getPrivateChannel().sendMessage("Failed").queue();
        });


    }

    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
