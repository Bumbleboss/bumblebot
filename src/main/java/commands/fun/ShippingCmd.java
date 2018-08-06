package commands.fun;

import java.awt.Color;
import java.util.Random;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

public class ShippingCmd extends Command {

	public ShippingCmd() {
		this.name = "ship";
		this.help = "Ship someone with someone else ;)";
		this.arguments = "<user> <user> {} @BumbleCore @MyWaifu";
		this.category = Bumblebot.Fun;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String m1;
		String m2;
		
		if(e.getArgs().isEmpty()){
			e.reply("You need to mention two people!");
			return;
		}
		
		String[] mm = e.getArgs().split("\\s+");
		EmbedBuilder eb = new EmbedBuilder();
		
		if(mm.length==1) {
			e.reply("You need to mention two people!");
			return;
		}
		
		m1 = mm[0].matches("<@!?(\\d+)>")?e.getMessage().getMentionedUsers().get(0).getName():mm[0];
		try{
			m2 = e.getMessage().getMentionedUsers().get(1).getName();
		}catch (IndexOutOfBoundsException ex) {
			m2 = mm[1].matches("<@!?(\\d+)>")?e.getMessage().getMentionedUsers().get(0).getName():mm[1];
		}

		eb.setColor(Color.decode(ConfigUtil.getHex()));
		eb.setDescription(getShipMsg(m1, m2));
		eb.setFooter(m1 + " ❤ " + m2, null);
		e.reply(eb.build());
	}
	
	private StringBuilder getShipMsg(String m1, String m2) {
		Random r = new Random();
		int Low = 1;
		int High = 100;
		int result = r.nextInt(High-Low);
		StringBuilder sb = new StringBuilder();
		
		if(result >= 1 && result <= 10) {
			sb.append("**").append(result).append("%** `​█         ​` It's not gonna work ;-;");
		}else if(result >= 11 && result <= 20) {
			sb.append("**").append(result).append("%** `​██        ​` Forget it...");
		}else if(result >= 21 && result <= 30) {
			sb.append("**").append(result).append("%** `​█​██       ​` Very bad.");
		}else if(result >= 31 && result <= 40) {
			sb.append("**").append(result).append("%** `​█​███      ​` Not too good.");
		}else if(result >= 41 && result <= 50) {
			sb.append("**").append(result).append("%** `█​████     ​` Almost");
		}else if(result >= 51 && result <= 60) {
			sb.append("**").append(result).append("%** `█​█████    ​` Almost");
		}else if(result >= 61 && result <= 68) {
			sb.append("**").append(result).append("%** `​█​██████   ​` Not too shabby!");
		}else if(result == 69) {
			sb.append("**").append(result).append("%** `​█​██████   ​` Oh I see how it is ( ͡° ͜ʖ ͡°)");
		}else if(result >= 70 && result <= 80) {
			sb.append("**").append(result).append("%** `​█​███████  ​` Take good care!");
		}else if(result >= 81 && result <= 90) {
			sb.append("**").append(result).append("%** `​█​████████ ​` My my, I'm so envious.");
		}else if(result >= 91 && result <= 99) {
			sb.append("**").append(result).append("%** `​█​████████ ​` Damn :o");
		}else if(result == 100) {
			sb.append("**").append(result).append("%** `​█​█████████​` SUGOOOOOI!! YOU TWO ARE MEANT FOR EACH OTHER!!");
		}else{
			sb.append("**").append(result).append("%** `​          ​` Okay, it's pointless.");
		}
		return sb;
	}
}
