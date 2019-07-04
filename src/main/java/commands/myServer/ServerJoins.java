package commands.myServer;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Objects;

import javax.imageio.ImageIO;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utility.ConfigUtil;
import utility.ImgUtil;
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
		if(e.getGuild().getId().equals(ConfigUtil.getServerId())) {
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
		if(e.getGuild().getId().equals(ConfigUtil.getServerId())) { 
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

	/*
	@SuppressWarnings({"unused", "WeakerAccess"})
	public static File getWelcomeMsg(User usr) {
		try {
			int x; int y;
			
			Image image = ImageIO.read(new File("./assists/imgs/server-join.png"));
			BufferedImage buffered_image_x = (BufferedImage) image;
				
			InputStream f = ImgUtil.imageFromUrl(Bumblebot.jda.getUserById(usr.getId()).getEffectiveAvatarUrl() +"?size=1024");
			Image image2 = ImageIO.read(f);
			BufferedImage buffered_image_y = (BufferedImage) image2;
				
			Image image3 = ImageIO.read(new File("./assists/imgs/server-join-trans.png"));
			BufferedImage buffered_image_z = (BufferedImage) image3;
				
			buffered_image_x.getGraphics().drawImage(ImgUtil.resize(buffered_image_y, 420, 420), 1075, 0, null);
			buffered_image_x.getGraphics().drawImage(buffered_image_z, 0, 0, null);
			
			File file = new File("./assists/imgs/joins/Welcome_"+UsrMsgUtil.stripFormatting(usr.getName())+".png");
			ImageIO.write(buffered_image_x, "PNG", file);
			
			
			return file;
			} catch (IOException ex) {
				OtherUtil.getWebhookError(ex, ServerJoins.class.getName(), null);
			}
		return null;	
	}
	*/

}
