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

        StringBuilder sb = new StringBuilder();
        sb.append("Owner: ").append("**"+guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator()+"**").append("\n");
        sb.append("Region: ").append("**"+guild.getRegion().getName()+"** " + guild.getRegion().getEmoji()).append("\n");
        sb.append("Verification Level: ").append("**"+guild.getVerificationLevel().name().substring(0,1).toUpperCase() + guild.getVerificationLevel().name().substring(1).toLowerCase()+"**");
        eb.addField("Info", sb.toString()+"\n", false);

        
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Categories: ").append("**"+guild.getCategories().size()+"**").append("\n");
        sb2.append("Text Channels: ").append("**"+guild.getTextChannels().size()+"**").append("\n");
        sb2.append("Voice channels: ").append("**"+guild.getVoiceChannels().size()+"**").append("\n");
        eb.addField("Channels", sb2.toString(), true);
        
        int[] all = UsrMsgUtil.getMembers(guild);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("All: ").append("**"+guild.getMembers().size()+"**").append("\n");
        sb3.append("Bots: ").append("**"+all[4]+"**").append("\n");
        sb3.append("Users: ").append("**"+all[5]+"**").append("\n");
        eb.addField("Members", sb3.toString(), true);
        
        String month = guild.getCreationTime().getMonth().name().substring(0,1).toUpperCase() + guild.getCreationTime().getMonth().name().substring(1).toLowerCase();
        eb.setFooter("Created on "+guild.getCreationTime().getDayOfMonth() + " " + month + ", " + guild.getCreationTime().getYear() + " | ID: " + guild.getId(), null);
        eb.setColor(Color.decode("#a88e79"));
		e.reply(eb.build());
	}
}
