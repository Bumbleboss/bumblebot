package commands.fun.nekoLife;

import java.awt.Color;
import java.util.Objects;

import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import utility.ConfigUtil;
import utility.OtherUtil;

public class HugNekoCmd extends Command {

	public HugNekoCmd() {
		this.name = "hug";
		this.help = "Hug someone >//<";
		this.category = Bumblebot.Fun;
	}
	
	@Override
	protected void execute(CommandEvent e) {
    	String[] selfmsg = new String[] {"Why so lonely, here's a hug (づ｡◕‿‿◕｡)づ", "Why are you hugging yourself? ;-;", "Have a hug from me instead!", "Don't hug yourself ;-;"};		
		String[] msg = new String[] {"just gave a hug to", "hugged", "just sent a hug to"};
		String message;	
		String img = null;
		
		if(e.getMessage().getMentionedUsers().size() > 0) {
			User user = e.getMessage().getMentionedUsers().get(0);
			if(e.getMessage().getMentionedUsers().get(0).getId().equals(e.getAuthor().getId())) {
				message = OtherUtil.getRandom(selfmsg) + " " + user.getAsMention();
			}else{
				message = e.getAuthor().getAsMention() + " "+OtherUtil.getRandom(msg)+" " + user.getAsMention();
			}
			img = new JSONObject(Objects.requireNonNull(OtherUtil.getGET("https://nekos.life/api/hug"))).getString("url");
		}else{
			message = "You need to mention a user!";
		}
		e.reply(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setDescription(message).setImage(img).build());
	}

}
