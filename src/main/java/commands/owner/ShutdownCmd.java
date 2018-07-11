package commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;

public class ShutdownCmd extends Command {
	
	public ShutdownCmd() {
		this.name = "shutdown";
        this.help = "Shutsdown the bot";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.category = Bumblebot.Owner;
        this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent e) {
		e.reply("Shutting down bot...");
		Bumblebot.logger.info("Bot was shutdown by user " + e.getAuthor().getId());
        e.getJDA().shutdown();
        System.exit(0);
	}
}
