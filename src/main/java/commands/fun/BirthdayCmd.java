package commands.fun;

import java.awt.Color;
import java.time.Instant;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

public class BirthdayCmd extends Command{

	public BirthdayCmd() {
		this.help = "Say happy birthday to someone!";
		this.aliases = new String[] {"bd"};
		this.arguments = "<user> {} @BumbleCore";
		this.name = "birthday";
		this.category = Bumblebot.Fun;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		if(e.getMessage().getMentionedUsers().size() > 0) {
			e.reply(new EmbedBuilder()
					.setDescription("HAPPY BIRTHDAY " + e.getMessage().getMentionedUsers().get(0).getName().toUpperCase())
					.setColor(Color.decode(ConfigUtil.getHex()))
					.setImage("https://media.tenor.com/images/d48f0a731ae8274f5752f059a5af8bfd/tenor.gif")
					.setTimestamp(Instant.now())
					.build());
			e.reply("🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂");
		}else{
			e.reply("You need to mention a user!");
		}
	}
}
