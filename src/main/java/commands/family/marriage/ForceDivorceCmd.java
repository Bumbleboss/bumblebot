package commands.family.marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;
import utility.core.UsrMsgUtil;

public class ForceDivorceCmd extends Command {

	public ForceDivorceCmd() {
		this.name = "fdivorce";
		this.help = "For *cough* the forced marriages.";
		this.category = Bumblebot.Marriage;
		this.hidden = true;
		this.ownerCommand = true;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		String user = e.getAuthor().getId();
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), mrg.getPartner(user));
		
		//IF USER IS A BOT
		if(e.getJDA().getUserById(user).isBot()) {
			e.reply(e.getAuthor().getAsMention() + " how come you got married to divorce yourself?");
			return;
		}
		
		//IF USER IS MARRIED
		if(mrg.isMarried(user)) {
			if(isInGuild) {
				e.reply(e.getAuthor().getAsMention()+" just divorced "+ e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
			}else{
				e.reply(e.getAuthor().getAsMention()+" just divorced **"+ UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
			}
			mrg.removeUser(mrg.getPartner(user));
			mrg.removeUser(user);
			return;
		}
		
		//IF USER IS NOT MARRIED
		e.getChannel().sendMessage(e.getAuthor().getAsMention() + " you're not married to anyone.").queue();
	}
}