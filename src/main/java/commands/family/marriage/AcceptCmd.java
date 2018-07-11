package commands.family.marriage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;
import utility.core.UsrMsgUtil;

public class AcceptCmd extends Command {

	public AcceptCmd() {
		this.name = "accept";
		this.help = "Accept the proposal from your loved one!" + "**Only works for whom is being proposed to.**";
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
			e.reply(e.getAuthor().getAsMention() + " how can a bot be proposed to by someone?!");
			return;
		}
		
		//IF THE USER HAS NOT BEEN PROPOSED
		if(mrg.getUser(user) == null) {
			e.reply(e.getAuthor().getAsMention() + " you're not being proposed to by anyone.");
			return;
		}
		
		//IF USER IS ALREADY PROPOSING
		if(mrg.isProposing(user)) {
			if(isInGuild) {
				e.reply(e.getAuthor().getAsMention()+" you're already proposing to "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getAuthor().getAsMention()+" you're already proposing to **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			return;
		}
		
		//IF USER IS BEING PROPOSED
		if(mrg.isProposedTo(user)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+0"));
			if(isInGuild) {
				e.reply(e.getAuthor().getAsMention()+" just got married to "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getAuthor().getAsMention()+" just got married to **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			mrg.setMarried(user, true);
			mrg.setMarried(mrg.getPartner(user), true);
			mrg.setMarriageDate(user, now.format(formatter) + "");
			mrg.setMarriageDate(mrg.getPartner(user), now.format(formatter));
		}
	}
}
