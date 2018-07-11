package commands.misc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message.Attachment;
import utility.ImgUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class HexCmd extends Command {

	public HexCmd() {
		this.name = "hex";
		this.help = "Know the color of a given hex code";
		this.arguments = "<hexcode> {} a88e79";
		this.aliases = new String[] {"color"};
		this.category = Bumblebot.Misc;
	}
	@Override
	protected void execute(CommandEvent e) {
		String hex = e.getArgs().replace("#", "");
		try {

			List<Attachment> att = e.getMessage().getAttachments();
			if(att.size() > 0) {
				if(att.get(0).isImage()) {
					BufferedImage img = ImageIO.read(ImgUtil.imageFromUrl(att.get(0).getUrl()));
					hex = Integer.toHexString(ImgUtil.getMostCommonColour(img).getRGB()).substring(2);
				}
			}else if(OtherUtil.isValidURL(e.getArgs())){
				BufferedImage img = ImageIO.read(ImgUtil.imageFromUrl(e.getArgs()));
				hex = Integer.toHexString(ImgUtil.getMostCommonColour(img).getRGB()).substring(2);
			}else if(hex.length() < 6 || 6 < hex.length()) {
				hex = "invalid";
			}
			
			e.reply(new EmbedBuilder().setTitle("#"+hex.toUpperCase())
					.setColor(Color.decode("#"+hex)).setImage("http://placehold.it/1024x1024.png/"+hex+"/"+hex)
					.setFooter("Requested by " + UsrMsgUtil.getUserSet(e.getJDA(), e.getAuthor().getId()), e.getAuthor().getAvatarUrl())
					.build());
			
		}catch (Exception ex) {
			if(ex.getMessage().equals("Too many common colors")) {
				e.reply("Too many common colors!");
			}else if(ex instanceof NumberFormatException) {
				e.reply("Invalid hex code!");
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
