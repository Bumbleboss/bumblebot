package commands.owner;


import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import gnu.trove.set.hash.TLongHashSet;
import main.Bumblebot;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.ReceivedMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;


public class SudoCmd extends Command {

	public SudoCmd() {
		this.name = "sudo";
		this.help = "Performs a command as if the user triggered that command";
		this.ownerCommand = true;
		this.arguments = "<userId|userMention> <command> [argument] {} 142322376808333312 kiss @BumbleCore";
		this.category = Bumblebot.Owner;
	}
	@Override
	protected void execute(CommandEvent e) {	
		if(e.getArgs().isEmpty()) {
			UsrMsgUtil.sendEMessage("You must provide **"+this.arguments.split("\\{}")[0]+"**", e.getChannel());
			return;
		}
		
		try {
			String id = null;
			if(e.getMessage().getMentionedUsers().size() > 1) {
				id = e.getMessage().getMentionedUsers().get(0).getId();
			}
			
			String[] args = e.getArgs().split("\\s+");
			String user = id==null?args[0]:id;
			String arg;try {arg = args[2];}catch (ArrayIndexOutOfBoundsException ex) {arg = "";}
			
			Command[] cmds = Bumblebot.getCommands();
            for (Command cmd : cmds) {
                if (args[1].equalsIgnoreCase(cmd.getName())) {
                    cmd.run(new CommandEvent(new MessageReceivedEvent(e.getJDA(), 0, getMessage(user, e.getMessage(), arg)), arg, Bumblebot.getCommandClient()));
                }
            }
			
		}catch (Exception ex) {
			if(ex instanceof ArrayIndexOutOfBoundsException | ex instanceof NumberFormatException) {
				UsrMsgUtil.sendEMessage("Usage: **"+ConfigUtil.getPrefix()+this.name + " "+ this.arguments.split("\\{}")[0] + "**", e.getChannel());
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
	
	private Message getMessage(String authorId, Message msg, String content) {
		return new ReceivedMessage(msg.getIdLong(), 
				msg.getChannel(), msg.getType(), msg.isWebhookMessage(), msg.mentionsEveryone(), 
				new TLongHashSet(msg.getMentionedUsers().stream().map(User::getIdLong).collect(Collectors.toList())),
				new TLongHashSet(msg.getMentionedRoles().stream().map(Role::getIdLong).collect(Collectors.toList())),
				msg.isTTS(), msg.isPinned(), content, msg.getNonce(),
				msg.getJDA().getUserById(authorId), 
				msg.getEditedTime(), msg.getReactions(), msg.getAttachments(), msg.getEmbeds());
	}
}
