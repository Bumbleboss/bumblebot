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

public class KissNekoCmd extends Command{

	public KissNekoCmd() {
		this.name = "kiss";
		this.help = "Kiss someone :kissing_heart:";
		this.category = Bumblebot.Fun;
	}
	
	@Override
	protected void execute(CommandEvent e) {	
		String[] selfmsg = new String[] {"Why are you kissing yourself? ;-;", "Have a kiss from me instead!", "Don't kiss yourself ;-;"};		
		String[] msg = new String[] {"just kissed", "kissed", "gave a kiss to", "french kissed"};
		String message;	
		String img = null;
		
		if(e.getMessage().getMentionedUsers().size() > 0) {
			User user = e.getMessage().getMentionedUsers().get(0);
			if(e.getMessage().getMentionedUsers().get(0).getId().equals(e.getAuthor().getId())) {
				message = OtherUtil.getRandom(selfmsg) + " " + user.getAsMention();
			}else{
				message = e.getAuthor().getAsMention() + " "+OtherUtil.getRandom(msg)+" " + user.getAsMention();
			}
			img = new JSONObject(Objects.requireNonNull(OtherUtil.getGET("https://nekos.life/api/kiss"))).getString("url");
		}else{
			message = "You need to mention a user!";
		}
		e.reply(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setDescription(message).setImage(img).build());
	}
}
