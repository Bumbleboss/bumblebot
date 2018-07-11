package commands.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class VoteCmd extends Command {

	public String[] NUMBERS = new String[]{"1\u20E3","2\u20E3","3\u20E3",
	        "4\u20E3","5\u20E3","6\u20E3","7\u20E3","8\u20E3","9\u20E3", "\uD83D\uDD1F"};
	
	public VoteCmd() {
		this.name = "vote";
		this.help = "Have the bot make a voting message or add reactions to the message that needs voting\nOnly **2** things are allowed to be voted!!";
		this.arguments = "(<messageId>|<message>;<choice1>;...) {}Choose your fetish;Boobs;Butts";
		this.category = Bumblebot.Mod;
		this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.botPermissions = new Permission[]{Permission.MESSAGE_ADD_REACTION};
	}
	
	@Override
	protected void execute(CommandEvent e) {		
		if(e.getArgs().isEmpty()) {
			UsrMsgUtil.sendEMessage("Usage: **"+ConfigUtil.getPrefix()+this.name + " "+ this.arguments.split("\\{}")[0] + "**", e.getChannel());
			return;
		}
		
		String[] parts = e.getArgs().split("\\;");
		
		if(parts.length == 1) {
			e.getChannel().getMessageById(parts[0]).queue(m -> {
				m.addReaction("yes:445598815806816257").queue();
				m.addReaction("no:445598834698092544").queue();
			}, m -> {e.reply("You need to provide a valid messageId!");});
			return;
		}
		
		if(parts.length > 0) {
			if(parts.length == 2) {
				e.reply("You only gave me one choice!");
				return;
			}
			
			if(parts.length > 6) {
				e.reply("You can only have **5** choices!");
				return;
			}
			
			String text = parts[0];
			StringBuilder sb = new StringBuilder();
			
			sb.append(text+"\n\n");
			for(int i = 1; i < parts.length; i++) {
				sb.append("**"+(i)+".** "  + parts[i]+"\n");
			}
			
			e.reply(sb.toString(), m -> {
				addReaction(m, parts.length+1);
			});
		}
	}
	
	private String getEmoji(int number){
        return NUMBERS[number];
    }
	
	private void addReaction(Message m, int choices) {
		for(int i = 0; i < choices-2; i++) {
			m.addReaction(getEmoji(i)).queue();
		}
	}
}
