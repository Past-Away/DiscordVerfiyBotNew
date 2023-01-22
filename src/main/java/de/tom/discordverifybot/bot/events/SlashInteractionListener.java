package de.tom.discordverifybot.bot.events;

import de.tom.discordverifybot.DiscordVerifyBot;
import de.tom.discordverifybot.events.PlayerJoinListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SlashInteractionListener extends ListenerAdapter {

    private DiscordVerifyBot plugin;

    public SlashInteractionListener(DiscordVerifyBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("verify")) {
            TextInput title = TextInput.create("verify-title", "Titel", TextInputStyle.SHORT).setMinLength(1).setMaxLength(255).setRequired(true).setPlaceholder("Schreibe hier deinen Namen").build();
            Modal modal = Modal.create("verify-modal", "Verify-Modal").addActionRows(ActionRow.of(title)).build();
            event.replyModal(modal).queue();
        } else if (event.getName().equalsIgnoreCase("get-verify")) {
            TextInput title = TextInput.create("getVerify-title", "Titel", TextInputStyle.SHORT).setMinLength(1).setMaxLength(255).setRequired(true).setPlaceholder("Schreibe hier den Namen").build();
            Modal modal = Modal.create("getVerify-modal", "User-Modal").addActionRows(ActionRow.of(title)).build();
            event.replyModal(modal).queue();
        } else if (event.getName().equalsIgnoreCase("remove-verify")) {
            event.replyEmbeds(ConfirmEmbed(event.getInteraction().getMember()).build()).addActionRow(Button.success("confirm", "Bestätige").withEmoji(Emoji.fromUnicode("U+2705"))).setEphemeral(true).queue();
        }
    }

    private EmbedBuilder ConfirmEmbed(Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String uuid = plugin.getVerifiedPlayersFile().getKey(member.getId());
        System.out.println("uuid: " + uuid);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setDescription("Bitte bestätige das du damit einverstanden bist, dass dein Minecraft account getrennt wird.");
        embedBuilder.addField("Minecraft UUID", player.getName(), false);
        embedBuilder.addField("Discord ID", member.getId(), true);
        embedBuilder.addField("Discord Name", member.getEffectiveName(), true);
        embedBuilder.setThumbnail("http://cravatar.eu/helmhead/"+uuid+"/600.png");
        embedBuilder.setFooter("Bestätige mit dem Button");
        return embedBuilder;
    }


}
