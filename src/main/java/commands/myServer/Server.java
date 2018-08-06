package commands.myServer;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.ConfigUtil;

public abstract class Server extends Command {

	protected Server() {
		this.category = Bumblebot.myServer;
	}
	
	 protected void execute(CommandEvent e) {
		 if(e.isOwner()) {
			 doCommand(e);
		 }else if(e.getGuild().getId().equals(ConfigUtil.getServerId())) {
			 doCommand(e);
		 }
	 }
	 
	 protected abstract void doCommand(CommandEvent event);
}
