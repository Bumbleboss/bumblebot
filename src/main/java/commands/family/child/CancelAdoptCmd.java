package commands.family.child;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Children;
import main.Bumblebot;

public class CancelAdoptCmd extends Command {

	public CancelAdoptCmd() {
		this.name = "adoptcancel";
		this.help = "Cancel the adoption before its too late!";
		this.arguments = "@child {} @NOTBumbleCore";
		this.category = Bumblebot.Marriage;
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
			if(chl.isAdopted(child)) {
				e.reply("You already have adopted " + e.getJDA().getUserById(child).getAsMention());
			}else{
				e.reply("You just canceled the adoption of " + e.getJDA().getUserById(child).getAsMention());
				chl.removeChild(child);
			}
			return;
		}
		
		e.reply("You are not the parent of the mentioned child.");
	}
}
