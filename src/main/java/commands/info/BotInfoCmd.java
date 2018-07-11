package commands.info;

import java.awt.Color;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class BotInfoCmd extends Command {

	public BotInfoCmd() {
		this.name = "botinfo";
		this.help = "Know more about me!";
		this.aliases = new String[] {"bi"};
		this.category = Bumblebot.Info;
	}
	@Override
	protected void execute(CommandEvent e) {
		int cmds = Arrays.asList(Bumblebot.getCommands()).stream().filter(cc -> !(cc.isOwnerCommand() && !e.isOwner())).collect(Collectors.toList()).size();
		EmbedBuilder eb = new EmbedBuilder();
	    eb.setAuthor("BumbleCore", null, e.getJDA().getSelfUser().getAvatarUrl());
	    eb.setColor(Color.decode(ConfigUtil.getHex()));
	    eb.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
	    eb.setDescription("Hello there! I'm **BumbleCore**. A bot that is dedicated for [this server](https://discord.gg/7PCdKYN)"
	    		+ "\nI will be your personal waifu! I can cook, wash, str--. *cough* I mean.. I will be your assistant!!"
	    		+ " I can play music, check users info and many more useless **"+cmds+"** functions! ver **"+Bumblebot.botVersion+"**"
	    		+ "\n\nTo know what functions I can do, type with **"+ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+"**"
	    		+ "\nFeel free to support me on [PayPal](https://www.paypal.me/bumbleboss) or [Patreon](https://www.patreon.com/bumblecore)"
	    		+ "\n\nNo idea what a command does? Type **"+ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" [command]**!");
	    eb.addField("OS", System.getProperty("os.name"), true);
	    Game gm =  e.getJDA().getPresence().getGame();
	   	eb.addField("Game", (gm != null) ? gm.getName() : "None", true);
	    eb.addField("Library", "JDA "+ JDAInfo.VERSION, true);
	    eb.addField("Ping", e.getJDA().getPing() + "ms", true);
	    eb.addField("Uptime", OtherUtil.getUptime(), true);
	    eb.setFooter("Developed by " + UsrMsgUtil.getUserSet(e.getJDA(), ConfigUtil.getOwnerId()) + " | Hosted by " + UsrMsgUtil.getUserSet(e.getJDA(), "229100974554218496"), null);
		e.reply(eb.build());
	}
}
