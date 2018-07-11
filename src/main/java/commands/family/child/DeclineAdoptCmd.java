package commands.family.child;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Children;
import main.Bumblebot;
import utility.core.UsrMsgUtil;

public class DeclineAdoptCmd extends Command {

	public DeclineAdoptCmd() {
		this.name = "adoptdecline";
		this.help = "Refusing of becoming a child of someone!";
		this.category = Bumblebot.Marriage;
		
	}
	
	@Override
	protected void execute(CommandEvent e) {
		Children chl = new Children();
		String child = e.getAuthor().getId();
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), chl.getParentA(child));
		
		if(e.getJDA().getUserById(child).isBot()) {
			e.reply("A bot cannot be adopted so how can there be consent for a bot in adoption?!");
			return;
		}
		
		if(chl.getChild(child) == null) {
			e.reply("You are not being adopted by anyone.");
			return;
		}
		
		if(chl.isAdopted(child)) {
			if(isInGuild) {
				e.reply("You're already adopted by " + e.getJDA().getUserById(chl.getParentA(child)).getAsMention());
			}else{
				e.reply("You're already adopted by **" + UsrMsgUtil.getUserSet(e.getJDA(), chl.getParentA(child)) + "**");
			}
			return;
		}
		
		if(isInGuild) {
			e.reply(e.getAuthor().getAsMention() + " just refused the adoption of " + e.getJDA().getUserById(chl.getParentA(child)).getAsMention());
		}else{
			e.reply(e.getAuthor().getAsMention() + " just refused the adoption of **" + UsrMsgUtil.getUserSet(e.getJDA(), chl.getParentA(child)) + "**");
		}
		chl.removeChild(child);	
	}
}