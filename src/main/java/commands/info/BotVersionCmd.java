package commands.info;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

@SuppressWarnings({"ConstantConditions", "MismatchedQueryAndUpdateOfStringBuilder", "MismatchedReadAndWriteOfArray"})
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
		String[] fx = new String[] {};
		String[] ds = new String[] {};
		
		eb.addField("Version", "Current version: **"+ Bumblebot.botVersion + "**", false);

		//noinspection ConstantConditions
		for (String aFt : ft) {
			sb.append("- ").append(aFt).append("\n");
		}

		//noinspection ConstantConditions
		for (String aFx : fx) {
			sb2.append("- ").append(aFx).append("\n");
		}

		//noinspection ConstantConditions
		for (String d : ds) {
			sb3.append("- ").append(d).append("\n");
		}

		eb.setColor(Color.decode(ConfigUtil.getHex()));
		e.reply(eb.build());
	}
}
