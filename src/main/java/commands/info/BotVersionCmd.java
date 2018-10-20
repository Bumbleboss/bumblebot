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
		
		String[] ft = new String[] {"new command, marriage statistics! **" + pr + "mstats**", "forced marriages now supports by id --OWNER"};
		String[] fx = new String[] {"some code clean up, removed duplicated code"};
		String[] ds = new String[] {};
		
		eb.addField("Version", "Current version: **"+ Bumblebot.botVersion + "**", false);

		if(ft.length > 0) {
			for (String aFt : ft) {
				sb.append("- ").append(aFt).append("\n");
			}
			eb.addField("Features", sb.toString(), false);
		}
			

		if(fx.length > 0) {
			for (String aFx : fx) {
				sb2.append("- ").append(aFx).append("\n");
			}
			eb.addField("Fixes", sb2.toString(), false);
		}

		if(ds.length > 0) {
			for (String d : ds) {
				sb3.append("- ").append(d).append("\n");
			}
			eb.addField("Disabled", sb3.toString(), false);
		}

		eb.setColor(Color.decode(ConfigUtil.getHex()));
		e.reply(eb.build());
	}
}
