package commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.OtherUtil;

public class RestartCmd extends Command {

	public RestartCmd() {
		this.name = "restart";
        this.help = "Restarts bot's instance";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.category = Bumblebot.Owner;
        this.hidden = true;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		e.reply("Restarting bot...");
		Bumblebot.logger.info("Bot was restarted by userID " + e.getAuthor().getId());
		OtherUtil.restart();
	}
}
