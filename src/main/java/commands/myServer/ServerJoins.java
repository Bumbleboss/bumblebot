package commands.myServer;

import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class ServerJoins extends ListenerAdapter {

	public void onMessageReceived(MessageReceivedEvent e) {
		if(OtherUtil.isOwners(e)) {
			if(e.getMessage().getContentRaw().startsWith(ConfigUtil.getPrefix()+"memwel ")) {
				String user = e.getMessage().getContentRaw().replace(ConfigUtil.getPrefix()+"memwel ", "");
				User usr = e.getJDA().getUserById(user);
				e.getGuild().getTextChannelById(ConfigUtil.getServerTC()).sendMessage(new EmbedBuilder()
						.setAuthor("Member Joined", null, usr.getEffectiveAvatarUrl())
						.setFooter(UsrMsgUtil.getUserSet(e.getJDA(), usr.getId()) +" | "+usr.getId(), null)
						.setTimestamp(Instant.now())
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setImage("https://cdn.discordapp.com/attachments/339727721376645120/517986545047961602/sample_join.png")
						.build()).queue();
			}
		}
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		if(e.getGuild().getIdLong() == ConfigUtil.getServerId()) {
			if(!e.getUser().isBot()) {
				e.getGuild().getController().addSingleRoleToMember(e.getMember(), e.getGuild().getRoleById("281034463410913280")).queue();
				User usr = e.getUser();
				e.getGuild().getTextChannelById(ConfigUtil.getServerTC()).sendMessage(new EmbedBuilder()
						.setAuthor("Member Joined", null, usr.getEffectiveAvatarUrl())	
						.setFooter(UsrMsgUtil.getUserSet(e.getJDA(), usr.getId()) +" | "+usr.getId(), null)
						.setTimestamp(Instant.now())
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setImage("https://cdn.discordapp.com/attachments/339727721376645120/517986545047961602/sample_join.png")
						.build()).queue();
			}
		}
	}
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
		if(e.getGuild().getIdLong() == ConfigUtil.getServerId()) { 
			if(!e.getUser().isBot()) {
				e.getGuild().getTextChannelById(ConfigUtil.getServerTC()).sendMessage(new EmbedBuilder()
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setAuthor("Member left!", null, e.getUser().getEffectiveAvatarUrl())
						.setImage("https://cdn.discordapp.com/attachments/339727721376645120/517986562991063052/sample_leave.png")
						.setFooter(UsrMsgUtil.getUserSet(e.getJDA(), e.getUser().getId()) + " | " + e.getUser().getId(), null)
						.setTimestamp(Instant.now())
						.build()).queue();
			}
		}	
	}
}
