package commands.info;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

public class BotVersionCmd extends Command {

	public BotVersionCmd() {
		this.name = "version";
		this.help = "Check the version of the bot along with it's updates notes";
		this.category = Bumblebot.Info;
		this.aliases = new String[] {"ver"};
	}
	
	@Override
	protected void execute(CommandEvent e) {
		EmbedBuilder eb = new EmbedBuilder();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		String pr = ConfigUtil.getPrefix();
		
		String[] ft = new String[] {};
		String[] fx = new String[] {"Uptime command help description emoji fix"};
		String[] ds = new String[] {};
		
		eb.addField("Version", "Current version: **"+ Bumblebot.botVersion + "**", false);
		
		for(int i = 0; i < ft.length;i++) {
			sb.append("- " + ft[i] + "\n");
		}
		
		for(int i = 0; i < fx.length;i++) {
			sb2.append("- " + fx[i] + "\n");
		}
		
		for(int i = 0; i < ds.length;i++) {
			sb3.append("- " + ds[i] + "\n");
		}
		
		if(ft.length > 0) {
			eb.addField("New features", sb.toString(), false);
		}
		
		if(fx.length > 0) {
			eb.addField("Fixes", sb2.toString(), false);	
		}
		
		if(ds.length > 0) {
			eb.addField("Disabled", sb3.toString(), false);	
		}
		
		eb.setColor(Color.decode(ConfigUtil.getHex()));		
		e.reply(eb.build());
	}
}
