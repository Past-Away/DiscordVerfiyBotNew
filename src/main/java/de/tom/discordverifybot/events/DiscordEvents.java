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
                if (plugin.getHikariCP().isPlayerInList(event.getAuthor().getId()) == false) {
                    System.out.println("not in DAtabase");
                    Player player = Bukkit.getPlayer(event.getMessage().getContentDisplay().toLowerCase());
                    if (!plugin.getRequestedCode().contains(event.getAuthor())) {
                        plugin.getRequestedCode().add(event.getAuthor());
                        String password = generateRandomPassword(8);
                        createVerifyPrivatEmbed(event, password);
                        plugin.getVerifyCodes().put(player, password);
                        plugin.getDiscordID().put(player, event.getAuthor().getId());
                        System.out.println(player.getUniqueId());
                        createVerifyEmbed(event);
                    } else {
                        createErrorAlreadyEmbed(event);
                    }

                } else {
                    System.out.println(plugin.getHikariCP().isPlayerInList(event.getAuthor().getId()));
                    System.out.println("allredy in DB");
                    createErrorEmbed(event);
                }
            } else {
                System.out.println("not on server");
            }
        }
    }

    public void createErrorEmbed(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("MC-Verification", null);
        eb.setColor(Color.red);
        eb.setColor(new Color(0xF40C0C));
        eb.setColor(new Color(255, 0, 54));

        eb.setDescription("Your DiscordID is already in use, write to a team member.");

        eb.setAuthor("T-Verification", null, null);

        eb.setFooter("T-Verification", null);

        event.getChannel().sendMessage(eb.build()).queue();
    }

    public void createErrorAlreadyEmbed(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("MC-Verification", null);
        eb.setColor(Color.red);
        eb.setColor(new Color(0xF40C0C));
        eb.setColor(new Color(255, 0, 54));

        eb.setDescription("You have already requested a code.");

        eb.setAuthor("T-Verification", null, null);

        eb.setFooter("T-Verification", null);

        event.getChannel().sendMessage(eb.build()).queue();
    }

    public void createVerifyEmbed(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("MC-Verification", null);
        eb.setColor(Color.red);
        eb.setColor(new Color(0x78F40C));
        eb.setColor(new Color(0, 255, 67));

        eb.setDescription("The bot has created a code for you, have a look at the private message");

        eb.setAuthor("T-Verification", null, null);

        eb.setFooter("T-Verification", null);

        event.getChannel().sendMessage(eb.build()).queue();
    }

    public void createVerifyPrivatEmbed(MessageReceivedEvent event, String password) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("MC-Verification", null);
        eb.setColor(Color.red);
        eb.setColor(new Color(0x0C8FF4));
        eb.setColor(new Color(0, 166, 255));

        eb.setDescription("Thank you for the verification, if you have any bugs or need help please contact the server team.\n" +
                "\n" +
                "Your code: " + password +
                "\n" +
                "\nUse /Verify " + password + " to verify your Discord account");

        eb.setAuthor("T-Verification", null, null);

        eb.setFooter("T-Verification", null);

        event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(eb.build()).queue(), t -> {
            event.getPrivateChannel().sendMessage("Failed").queue();
        });

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
    public static void sendPM(TextChannel channel, User user, String message, String fail) {
        user.openPrivateChannel().queue(pc -> pc.sendMessage(message).queue(), t -> {
            channel.sendMessage(fail).queue();
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
