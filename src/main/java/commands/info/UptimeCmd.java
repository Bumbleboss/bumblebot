package commands.info;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;

public class UptimeCmd extends Command {

	public UptimeCmd() { 
		this.name = "uptime";
		this.help = "Check for how long I have been running. :sweat_drops:";
		this.category = Bumblebot.Info;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		e.reply(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setDescription(OtherUtil.getUptime()).build());
	}
}
