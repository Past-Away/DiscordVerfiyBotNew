package de.tom.discordverifybot.bot;

import de.tom.discordverifybot.DiscordVerifyBot;
import de.tom.discordverifybot.bot.events.ButtonInteractionListener;
import de.tom.discordverifybot.bot.events.ModalInteractionListener;
import de.tom.discordverifybot.bot.events.SlashInteractionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.logging.Level;

public class Bot {

    private JDA jda;

    private DiscordVerifyBot plugin;
    private boolean tokenSet = false;
    public Bot(String token, DiscordVerifyBot plugin) {
        this.plugin = plugin;
        try {
            jda = JDABuilder.createDefault(token).build();
            jda.addEventListener(new SlashInteractionListener(plugin));
            jda.addEventListener(new ModalInteractionListener(plugin));
            jda.addEventListener(new ButtonInteractionListener(plugin));
            jda.upsertCommand("verify", "Verifiziere dich auf dem Server").queue();
            jda.upsertCommand("get-verify", "Erhalte die Spieler daten").queue();
            jda.upsertCommand("remove-verify", "Trenne die Verbindung zu deinem Minecraft Account").queue();
            tokenSet = true;
        }catch (Exception e) {
            plugin.getLogger().log(Level.INFO,plugin.getPrefix() + "Der Token ist ungültig!");
            tokenSet = false;
        }
    }
    public JDA getJda() {
        return jda;
    }

    public DiscordVerifyBot getPlugin() {
        return plugin;
    }

    public void shutdown() {
        if (tokenSet) {
            jda.shutdownNow();
        }
    }

    public void addRoleToUser(User user) {
        jda.getGuildById(plugin.getConfig().getString("discord.guildId")).addRoleToMember(user, jda.getRoleById(plugin.getConfig().getString("discord.verifyRole"))).queue();
    }
}
