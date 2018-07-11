package commands.misc;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import urbanAPI.UrbanAPI;
import urbanAPI.entities.UrbanException;
import urbanAPI.entities.UrbanList;
import utility.ConfigUtil;
import utility.OtherUtil;

public class UrbanCmd extends Command {

	public UrbanCmd() {
		this.name = "define";
		this.aliases = new String[] {"urban", "ud"};
		this.help = "Get to know what a word means from Urban Dictionary!";
		this.arguments = "<word> {} Whomst'dve";
		this.category = Bumblebot.Misc;
	}

	@Override
	protected void execute(CommandEvent e) {
		if(e.getArgs().isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}
		
		UrbanAPI inf = ConfigUtil.ub;
		try {
			UrbanList urb = inf.getUrbanInfo(e.getArgs()).getInfo().get(0);
			EmbedBuilder eb = new EmbedBuilder();
				
			String perm = urb.permalink;
			eb.setAuthor("Urban Dictionary", perm, "http://rf32.nl/img/funsites/256/urbandictionairy.png");
				
			String def = urb.definition.replace("\r", "");
			eb.addField("Definition", def.length() > 900 ? def.substring(900) + " ...[Read more]("+ perm+")" : def, false);
				
			String exp = urb.example.replace("\r", "");
			eb.addField("Example", exp.isEmpty() ? "N/A" : (exp.length() > 900? exp.substring(900) + " ...[Read more]("+ perm+")" : exp), false);
				
			eb.setFooter("👍 " + OtherUtil.getCount(""+urb.thumbsUp+"") + " | 👎 " + OtherUtil.getCount(urb.thumbsDown+""), null);
			eb.setColor(Color.decode(ConfigUtil.getHex()));
			e.reply(eb.build());
			} catch (Exception ex) {
				if(ex instanceof UrbanException) {
					e.reply(new EmbedBuilder().setDescription("No results were found. :cry:").build());
				}else{
					OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
				}
			}
		
	}
}
