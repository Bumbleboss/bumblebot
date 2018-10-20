package commands.family.child;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Children;
import main.Bumblebot;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class AcceptAdoptCmd extends Command {

	public AcceptAdoptCmd() {
		this.name = "adoptaccept";
		this.help = "Accept of becoming a child of someone!";
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

		if(OtherUtil.childArguement(chl, child, isInGuild, e)) {return;}
		
		chl.setAdoption(child, true);
		if(isInGuild) {
			e.reply(e.getAuthor().getAsMention() + " just got adopted by " + e.getJDA().getUserById(chl.getParentA(child)).getAsMention());
		}else{
			e.reply(e.getAuthor().getAsMention() + " just got adopted by **" + UsrMsgUtil.getUserSet(e.getJDA(), chl.getParentA(child)) + "**");
		}
	}
}
