package commands.family.marriage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;

public class ForceMarryCmd extends Command {

	public ForceMarryCmd() {
		this.name = "fmarry";
		this.help = "Ever heard of force marriage? Well, this one is.";
		this.aliases = new String[] {"enslave"};
		this.category = Bumblebot.Marriage;
		this.hidden = true;
		this.ownerCommand = true;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		String user = e.getAuthor().getId();
		String user2 = null;
		
		
		if(e.getMessage().getMentionedUsers().size() > 0) {
			user2 = e.getMessage().getMentionedUsers().get(0).getId();
		}
		
		if(user2 == null) {
			e.reply("You need to mention someone!");
			return;
		}
		
		try {
			mrg.removeUser(mrg.getPartner(user));
			mrg.removeUser(user);
				
			mrg.removeUser(mrg.getPartner(user2));
			mrg.removeUser(user2);
		}catch (NullPointerException ex) {}
			
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+0"));
			
		mrg.addUser(user, user2, true, now.format(formatter), Marriage.STATUS.PROPOSER, true);
		mrg.addUser(user2, user, true, now.format(formatter), Marriage.STATUS.PROPOSEDTO, true);
			
		e.reply(e.getAuthor().getAsMention() + " just got married to " + e.getJDA().getUserById(user2).getAsMention() + " by force");
	}
}
