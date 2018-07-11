package commands.myServer;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

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
				File file = getWelcomeMsg(usr);
				e.getChannel().sendFile(file, file.getName()).embed(new EmbedBuilder()
						.setAuthor("Member Joined", null, usr.getEffectiveAvatarUrl())	
						.setFooter(UsrMsgUtil.getUserSet(e.getJDA(), usr.getId()) +" | "+usr.getId(), null)
						.setTimestamp(Instant.now())
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setImage("attachment://"+file.getName())
						.build()).queue();
			}
		}
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		if(e.getGuild().getId().equals(ConfigUtil.getServerId())) {
			if(e.getUser().isBot()) {
			}else{
				e.getGuild().getController().addSingleRoleToMember(e.getMember(), e.getGuild().getRoleById("281034463410913280")).queue();
				User usr = e.getUser();
				File file = getWelcomeMsg(usr);
				e.getGuild().getTextChannelById(ConfigUtil.getServerTC()).sendFile(file, file.getName()).embed(new EmbedBuilder()
						.setAuthor("Member Joined", null, usr.getEffectiveAvatarUrl())	
						.setFooter(UsrMsgUtil.getUserSet(e.getJDA(), usr.getId()) +" | "+usr.getId(), null)
						.setTimestamp(Instant.now())
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setImage("attachment://"+file.getName())
						.build()).queue();
			}
		}
	}
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
		if(e.getGuild().getId().equals(ConfigUtil.getServerId())) { 
			if(e.getUser().isBot()) {
			}else{
				e.getGuild().getTextChannelById(ConfigUtil.getServerTC()).sendMessage(new EmbedBuilder()
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setAuthor("Member left!", null, e.getUser().getEffectiveAvatarUrl())
						.setImage("https://cdn.discordapp.com/attachments/312197256399028224/426899093084700672/Server_leave.png")
						.setFooter(UsrMsgUtil.getUserSet(e.getJDA(), e.getUser().getId()) + " | " + e.getUser().getId(), null)
						.setTimestamp(Instant.now())
						.build()).queue();
			}
		}	
	}
	
	@SuppressWarnings("unused")
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
				
			buffered_image_x.getGraphics().drawImage(ImgUtil.resize(buffered_image_y, 420, 420), x = 1075, y = 0, null);
			buffered_image_x.getGraphics().drawImage(buffered_image_z, x = 0, y = 0, null);
			
			File file = new File("./assists/imgs/joins/Welcome_"+UsrMsgUtil.stripFormatting(usr.getName())+".png");
			ImageIO.write(buffered_image_x, "PNG", file);
			
			
			return file;
			} catch (IOException ex) {
				OtherUtil.getWebhookError(ex, ServerJoins.class.getName(), null);
			}
		return null;	
	}
}
