package commands.info;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import utility.core.UsrMsgUtil;

public class ServerInfoCmd extends Command {
	
	public ServerInfoCmd() {
		this.name = "serverinfo";
		this.help = "Get a detailed info about this server.";
		this.aliases = new String[] {"srvrinf", "si"};
		this.category = Bumblebot.Info;
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent e) {
		EmbedBuilder eb = new EmbedBuilder();
		Guild guild = e.getGuild();
        
        eb.setAuthor(guild.getName(), null, guild.getIconUrl());
        eb.setThumbnail(guild.getIconUrl());

        String sb = "Owner: **" + guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator() + "**\n" +
                	"Region: **" + guild.getRegion().getName() + "** " + guild.getRegion().getEmoji() + "\n" +
                	"Verification Level: **" + guild.getVerificationLevel().name().substring(0, 1).toUpperCase() + guild.getVerificationLevel().name().substring(1).toLowerCase() + "**";
        eb.addField("Info", sb, false);


        String sb2 = "Categories: **" + guild.getCategories().size() + "**\n" +
				     "Text Channels: **" + guild.getTextChannels().size() + "**\n" +
                	 "Voice channels: **" + guild.getVoiceChannels().size() + "**\n";
        eb.addField("Channels", sb2, true);
        
        int[] all = UsrMsgUtil.getMembers(guild);
        String sb3 = "All: **" + guild.getMembers().size() + "**\n" +
                	 "Bots: **" + all[4] + "**\n" +
                	 "Users: **" + all[5] + "**\n";
        eb.addField("Members", sb3, true);
        
        String month = guild.getCreationTime().getMonth().name().substring(0,1).toUpperCase() + guild.getCreationTime().getMonth().name().substring(1).toLowerCase();
        eb.setFooter("Created on "+guild.getCreationTime().getDayOfMonth() + " " + month + ", " + guild.getCreationTime().getYear() + " | ID: " + guild.getId(), null);
        eb.setColor(Color.decode("#a88e79"));
		e.reply(eb.build());
	}
}
