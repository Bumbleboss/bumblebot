package commands.family.child;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Children;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class ChildrenCmd extends Command {

	public ChildrenCmd() {
		this.name = "children";
		this.help = "Check how many childrens you have. ;)";
		this.category = Bumblebot.Marriage;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		Children chl = new Children();
		StringBuilder sb = new StringBuilder();
		String id = null;
		
		if(e.getMessage().getMentionedUsers().size() > 0) {
			id = e.getMessage().getMentionedUsers().get(0).getId();
		}else{
			id = e.getAuthor().getId();
		}
		
		for(int i = 0; i < chl.getChildren().size(); i++) {
			if((!chl.getChildren().get(i).getString("parentA").equals(id) && !chl.getChildren().get(i).getString("parentB").equals(id)) || !chl.getChildren().get(i).getBoolean("isAdopted")) {
			     continue;
			}
			
			if(!UsrMsgUtil.isInGuild(e.getGuild(), chl.getChildren().get(i).getString("id"))) {
				sb.append("**"+UsrMsgUtil.getUserSet(e.getJDA(), chl.getChildren().get(i).getString("id"))+"**").append(", ");
			}
			if(UsrMsgUtil.isInGuild(e.getGuild(), chl.getChildren().get(i).getString("id"))) {
				sb.append(e.getJDA().getUserById(chl.getChildren().get(i).getString("id")).getAsMention()).append(", ");
			}
		}
		
		String des = sb.toString();
		if(des.isEmpty()) {
			e.reply(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setTitle("Children").setDescription("You have no children").build());
		}else{
			e.reply(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setTitle("Children").setDescription(des.substring(0, des.length()-2)).build());
		}
	}
}
