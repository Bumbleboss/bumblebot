package commands.family.marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class DeclineCmd extends Command {
	
	public DeclineCmd() {
		this.name = "decline";
		this.help = "Reject them c:\n"+"**Only works for whom is being proposed to.**";
		this.category = Bumblebot.Marriage;
	}

	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		String user = e.getAuthor().getId();
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), mrg.getPartner(user));
		
		//IF USER IS ALREADY MARRIED
		if(mrg.isMarried(user)) {
			if(isInGuild) {
				e.reply(e.getAuthor().getAsMention()+" you're already married to "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getAuthor().getAsMention()+" you're already married to **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			return;
		}
		
		//IF USER IS A BOT
		if(e.getJDA().getUserById(user).isBot()) {
			e.reply(e.getAuthor().getAsMention() + " how can a bot be proposed to by someone?!");
			return;
		}
		
		//IF THE USER HAS NOT BEEN PROPOSED TO BY ANYONE
		if(mrg.getUser(user) == null) {
			e.reply(e.getAuthor().getAsMention() + " you're not being proposed to by anyone.");
			return;
		}
		
		//IF USER IS ALREADY PROPOSING
		if(mrg.isProposing(user)) {
			e.reply(e.getAuthor().getAsMention() + " you can cancel the proposal using **"+ConfigUtil.getPrefix()+new CancelCmd().getName()+"**.");
			return;
		}
		
		//IF USER IS BEING PROPOSED
		if(mrg.isProposedTo(user)) {
			if(isInGuild) {
				e.reply(e.getMessage().getAuthor().getAsMention()+" just rejected "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getMessage().getAuthor().getAsMention()+" just rejected **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			mrg.removeUser(mrg.getPartner(user));
			mrg.removeUser(user);
		}
	}
}
