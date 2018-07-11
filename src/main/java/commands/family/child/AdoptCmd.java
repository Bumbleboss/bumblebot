package commands.family.child;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Children;
import commands.family.Marriage;
import main.Bumblebot;
import utility.core.UsrMsgUtil;

public class AdoptCmd extends Command {
	
	public AdoptCmd() {
		this.name = "adopt";
		this.help = "Get your own child!";
		this.arguments = "@user {} @NOTBumbleCore";
		this.category = Bumblebot.Marriage;
	}

	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		Children chl = new Children();
		
		String user = e.getAuthor().getId();
		String child = null;
		if(e.getMessage().getMentionedUsers().size() > 0) {
			child = e.getMessage().getMentionedUsers().get(0).getId();
		}
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), chl.getParentA(child));
		boolean isInGuild2 = UsrMsgUtil.isInGuild(e.getGuild(), chl.getParentB(child));

		
		if(child == null) {
			e.reply("You need to mention someone!");
			return;
		}
		
		//IF ADOPT IS SELF
		if(child.equals(user)) {
			e.reply("You cannot adopt yourself!");
			return;
		}
		
		//IF ADOPT IS MYBOT
		if(child.equals(e.getJDA().getSelfUser().getId())) {
			e.reply("I can never be adopted by someone!");
			return;
		}
		
		//IF ADOPT IS BOT
		if(e.getJDA().getUserById(child).isBot()) {
			e.reply("You cannot adopt bots!");
			return;
		}
		
		//IF ADOPT IS PARENT
		if(chl.getChild(user) != null) {
			if(child.equals(chl.getParentA(user)) || child.equals(chl.getParentB(user))) {
				e.reply("You want to adopt your parent?....");
				return;
			}
		}
		
		//IF ADOPT IS A PARENTB OF PARTNER
		String part = mrg.getPartner(user);
		if(part!= null) {
			if(chl.getParentB(part) != null) {
				if(chl.getParentB(part).equals(child)) {
					e.reply("You want to adopt your partner's parent?....");
					return;
				}
			}
			if(chl.getParentA(part) != null) {
				if(chl.getParentA(part).equals(child)) {
					e.reply("You want to adopt your partner's parent?....");
					return;
				}
			}
		}	
			
		
		//IF ADOPT IS PARTNER
		if(child.equals(mrg.getPartner(user))) {
			if(mrg.isMarried(user)) {
				e.reply("How can you possibly adopt the one you're married to?!");
			}else if(mrg.isProposedTo(user)) {
				e.reply("How can you possibly adopt the one you are being proposed to?!");
			}else if(mrg.isProposing(user)) {
				e.reply("How can you possibly adopt the one you are proposing to?!");
			}
			return;
		}
		
		//IF ADOPT EXISTS
		if(chl.getChild(child) != null) {
			//IF ADOPT IS ADOPTED
			if(chl.isAdopted(child)) {
				if(user.equals(chl.getParentA(child)) || user.equals(chl.getParentB(child))) {
					e.reply("You already adopted " + e.getJDA().getUserById(child).getAsMention() +", or did you forget about that?");
				}else if(isInGuild || isInGuild2) {
					e.reply(e.getJDA().getUserById(child).getAsMention() + " is already adopted by " + e.getJDA().getUserById(chl.getParentA(child)).getAsMention());
				}else{
					e.reply(e.getJDA().getUserById(child).getAsMention() + " is already adopted by **" + UsrMsgUtil.getUserSet(e.getJDA(), chl.getParentA(child)) + "**");
				}
				return;
			}else{
				if(user.equals(chl.getParentA(child)) || user.equals(chl.getParentB(child))) {
					e.reply("You are already in the process of adopting " + e.getJDA().getUserById(child).getAsMention() +", or did you forget about that?");
				}else if(isInGuild || isInGuild2) {
					e.reply(e.getJDA().getUserById(child).getAsMention() + " is in the process of being adopted by " + e.getJDA().getUserById(chl.getParentA(child)).getAsMention());
				}else{
					e.reply(e.getJDA().getUserById(child).getAsMention() + " is in the process of being adopted by **" + UsrMsgUtil.getUserSet(e.getJDA(), chl.getParentA(child)) + "**");
				}
				return;
			}
		}
		
		//IF USER IS MARRIED
		if(mrg.isMarried(user)) {
			chl.addChild(child, mrg.getUser(user), mrg.getPartner(user), false);
			e.reply(e.getJDA().getUserById(user).getAsMention() + " just filed an adopt for " + e.getJDA().getUserById(child).getAsMention());
			return;
		}
		
		//USER NOT MARRIED
		e.reply("You may not adopt a child unless you are married!");
	}
}
