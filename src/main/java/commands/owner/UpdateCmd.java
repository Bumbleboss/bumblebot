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
		File upd = new File("update.jar");
		UsrMsgUtil.sendVEMessage("Updating bot!", e.getChannel());
		try {
			if(upd.exists()) {
				Runtime.getRuntime().exec("java -jar "+upd.getName());
				e.getJDA().shutdown();
				System.exit(0);
			}else{
				e.reply("`" + upd.getName() + "` is not found!");
			}
		} catch (IOException ex) {
			OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
		}
	}
}
