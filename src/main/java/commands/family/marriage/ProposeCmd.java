package commands.family.marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class ProposeCmd extends Command {

	public ProposeCmd() {
		this.name = "propose";
		this.help = "Propose to the partner of your dreams!\n"+"*If you are married, then it's time to divorce!*";
		this.arguments = "@user {} @NOTBumbleCore";
		this.category = Bumblebot.Marriage;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		String user = e.getAuthor().getId();
		String user2 = null;
		if(e.getMessage().getMentionedUsers().size() > 0) {
			user2 = e.getMessage().getMentionedUsers().get(0).getId();
		}
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), mrg.getPartner(user));
		boolean isInGuild2 = UsrMsgUtil.isInGuild(e.getGuild(), mrg.getPartner(user2));
		
		if(user2 == null) {
			e.reply("You need to mention someone!");
			return;
		}
		//IF IT'S ME
		if(user2.equals(e.getJDA().getSelfUser().getId())) {
			if(e.getAuthor().getId().equals(ConfigUtil.getOwnerId())) {
				e.reply(e.getAuthor().getAsMention() + " you already are my husband! ><");
			}else{
				e.reply(e.getAuthor().getAsMention() + " I am already married to my creator!");
			}
			return;
		}
		
		//IF USER IS A BOT
		if(e.getAuthor().isBot()) {
			e.reply(e.getAuthor().getAsMention() + " no bot is allowed to get married except me >.>");
			return;
		}
		
		//IF MENTIONED USER IS A BOT
		if(e.getJDA().getUserById(user2).isBot()) {
			e.reply("You cannot propose to a bot.");
			return;
		}
		
		
		//IF MENTIONED USER IS ALREADY MARRIED
		if(OtherUtil.marriageArguement(mrg, user2, isInGuild2, e, "married", false, false)) {
			return;
		}
		
		//IF USER IS ALREADY MARRIED
		if(OtherUtil.marriageArguement(mrg, user, isInGuild, e, "married", false, true)) {
			return;
		}
		
		//IF PROPOSES TO SELF
		if(user.equals(user2)) {
			e.getChannel().sendMessage(e.getAuthor().getAsMention()+" you cannot propose to yourself.").queue();
			return;
		}
		
		//IF USER IS ALREADY PROPOSING
		if(OtherUtil.marriageArguement(mrg, user, isInGuild, e, "propose", false, true)) {
			return;
		}
		
		//IF USER IS ALREADY BEING PROPOSEDTO
		if(mrg.isProposedTo(user)) {
			if(isInGuild) {
				e.reply(e.getAuthor().getAsMention()+" you're already being proposed to by "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getAuthor().getAsMention()+" you're already being proposed to by **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			return;
		}
		
		//IF MENTIONED USER IS ALREADY PROPOSING
		if(OtherUtil.marriageArguement(mrg, user2, isInGuild2, e, "propose", false, false)) {
			return;
		}
		
		//IF MENTIONED USER IS ALREADY BEING PROPOSEDTO 
		if(mrg.isProposedTo(user2)) {
			if(isInGuild2) {
				e.reply(e.getJDA().getUserById(user2).getAsMention()+" is already being proposed to by "+e.getJDA().getUserById(mrg.getPartner(user2)).getAsMention());
			}else{
				e.reply(e.getJDA().getUserById(user2).getAsMention()+" is already being proposed to by **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user2))+"**");
			}
			return;
		}
		
		//FINALLY GET'S PROPOSED
		mrg.addUser(user, user2, false, null, Marriage.STATUS.PROPOSER, false);
		mrg.addUser(user2, user, false, null, Marriage.STATUS.PROPOSEDTO, false);
		e.reply(e.getJDA().getUserById(mrg.getUser(user)).getAsMention()+" just proposed to " + e.getJDA().getUserById(mrg.getUser(user2)).getAsMention());
	}
}
