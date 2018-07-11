package commands.family.child;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Children;
import main.Bumblebot;

public class AbandonCmd extends Command {

	public AbandonCmd() {
		this.name = "abandon";
		this.help = "Leave the child you adopted ;-;";
		this.category = Bumblebot.Marriage;
		this.arguments = "@child {} @NOTBumbleCore";
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String user = e.getAuthor().getId();
		Children chl = new Children();
		
		String child = null;
		if(e.getMessage().getMentionedUsers().size() > 0) {
			child = e.getMessage().getMentionedUsers().get(0).getId();
		}
				
		if(child == null) {
			e.reply("You need to mention someone!");
			return;
		}
		
		if(chl.isParent(chl.getChild(child), user)) {
			e.reply("You just abandoned " + e.getJDA().getUserById(child).getAsMention() +" ;-;");
			chl.removeChild(child);
			return;
		}
		
		e.reply("You are not the parent of the mentioned child.");
	}
}
