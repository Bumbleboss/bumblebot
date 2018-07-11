package commands.info;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import utility.ConfigUtil;

public class AvatarCmd extends Command {

	public AvatarCmd() { 
		this.name = "avatar";
		this.help = "Get an avatar of a user.";
		this.aliases = new String[] {"ava", "ua"};
		this.arguments = "<@user> | <ID> {} @BumbleCore";		
		this.category = Bumblebot.Info;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		User user;
		if(e.getMessage().getMentionedUsers().size() > 0) {
			user = e.getMessage().getMentionedUsers().get(0);
		}else if(!e.getArgs().isEmpty()) {
			user = e.getJDA().retrieveUserById(e.getArgs()).complete();
		}else{
			user = e.getAuthor();
		}
		
		e.reply(new EmbedBuilder()
				.setColor(Color.decode(ConfigUtil.getHex()))
				.setAuthor(user.getName(), user.getAvatarUrl())
				.setFooter("Lookin' hot", null)
				.setImage(user.getAvatarUrl()+"?size=2048").build());
	}
}
