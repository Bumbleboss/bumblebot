package commands.family.marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;
import utility.core.UsrMsgUtil;


public class CancelCmd extends Command {
	
	public CancelCmd() {
		this.name = "cancel";
		this.help = "Cancel the proposal before it's too late";
		this.category = Bumblebot.Marriage;
	}

	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		String user = e.getAuthor().getId();
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), mrg.getPartner(user));

		//IF USER IS MARRIED
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
			e.reply(e.getAuthor().getAsMention() + " how can a bot be proposing to someone?!");
			return;
		}
		
		//IF USER IS NOT PROPOSING TO ANYONE
		if(mrg.getUser(user) == null) {
			e.reply(e.getAuthor().getAsMention() + " you're not proposing to anyone.");
			return;
		}
		
		//IF USER IS BEING PROPOSED
		if(mrg.isProposedTo(user)) {
			e.reply(e.getAuthor().getAsMention() + " you can reject the proposal using **>decline**.");
			return;
		}
		
		//IF USER IS PROPOSING
		if(mrg.isProposing(user)) {
			if(isInGuild) {
				e.reply(e.getMessage().getAuthor().getAsMention()+" just canceled the proposal to "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getMessage().getAuthor().getAsMention()+" just canceled the proposal to **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			mrg.removeUser(mrg.getPartner(user));
			mrg.removeUser(user);
		}
	}
}
