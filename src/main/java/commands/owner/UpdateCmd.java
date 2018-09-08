package commands.owner;

import java.io.File;
import java.io.IOException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class UpdateCmd extends Command {

	public UpdateCmd() {
		this.name = "update";
		this.help = "update the bot files";
		this.ownerCommand = true;
		this.category = Bumblebot.Owner;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		UsrMsgUtil.sendVEMessage("Updating bot!", e.getChannel());
		try {
			Runtime.getRuntime().exec("./update");
			e.getJDA().shutdown();
			System.exit(0);
		} catch (IOException ex) {
			OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
		}
	}
}
