package utility.jdu;

import java.awt.Color;
import java.util.*;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.Command.Category;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utility.ConfigUtil;
import utility.OtherUtil;

public class HelpUtil extends ListenerAdapter{
	
    private final List<Command> cmds = Bumblebot.getCommandClient().getCommands();
	
	public void onMessageReceived(MessageReceivedEvent s) {
		StringBuilder category = new StringBuilder();
		String cat = null;
		for(int i = 0; i < cmds.size(); i++) {
			if(cmds.get(i).getCategory().equals(Bumblebot.myServer)) {
				if(s.getChannelType().equals(ChannelType.PRIVATE)) {
					continue;
				}else{
					if(s.getGuild().getIdLong() != ConfigUtil.getServerId()) {
						continue;
					}
				}
			}
			
			if(cmds.get(i).getCategory().equals(Bumblebot.Mod)) {
				if(s.getChannelType().equals(ChannelType.PRIVATE)) {
					continue;
				}else{
					try {
						if(!s.getMember().hasPermission(Bumblebot.modPerms)) {
							continue;
						}
					}catch (NullPointerException ex) {
						continue;
					}
				}
			}
			
			if(cmds.get(i).isHidden() && !OtherUtil.isOwners(s)) {
				continue;
			}
			
			if(cmds.get(i).isOwnerCommand() && !OtherUtil.isOwners(s)) {
				continue;
			}
		
			if(s.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" "+ getHelp().get(i).getName())) {
				s.getChannel().sendMessage(getHelpMessage(i)).queue();
			}else{ 
				String[] lsts = getHelp().get(i).getAliases();
				for (String lst : lsts) {
					if (s.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix() + ConfigUtil.getHelpWord() + " " + lst)) {
						s.getChannel().sendMessage(getHelpMessage(i)).queue();
					}
				}
			} 
			
			if(s.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" " + cmds.get(i).getCategory().getName())) {
				cat = cmds.get(i).getCategory().getName();
				category.append(getCategoryMessage(cmds.get(i).getCategory(), i));
			}
		}
		
		if(category.length() > 0) {
			s.getChannel().sendMessage(new EmbedBuilder().setTitle(cat + " commands")
					.setDescription(category)
					.setColor(Color.decode(ConfigUtil.getHex()))
					.setFooter("To know more details about a command: " + ConfigUtil.getPrefix() + ConfigUtil.getHelpWord() + " <command>" , null).build()).queue();
		}
	}
	
	private String getCategoryMessage(Category cat, int i) {
		if(getHelp().get(i).getCategory().equals(cat)) {
			String helpDescription;
			if(getHelp().get(i).getHelp().length() > 30) {
				helpDescription = getHelp().get(i).getHelp().substring(0, 30).replace("*", "").replace("\n", " ") + "...";
			}else{
				helpDescription = getHelp().get(i).getHelp().replace("*", "").replace("\n", "");
			}
			return "**"+ getHelp().get(i).getName() + "** - *" + helpDescription+"*\n";
		}
		
		return null;
	}

	private List<Command> getHelp() {
		return this.cmds;
	}

	private Message getHelpMessage(int i) {
		
		String hexCode = ConfigUtil.getHex();
		Command help = getHelp().get(i);
		
		//COMMAND NAME
		String cmd = help.getName();
		//COMMAND DESCRIPTION
		String des = help.getHelp();
		
		//COMMAND ARGUMENT
		String[] argu = null;
		try {
			argu = help.getArguments().split("\\{}");
		}catch (NullPointerException ignored) {}
		//COMMAND ALIASES
		
		String[] alias = null;
		try {
			alias = help.getAliases();
		}catch (NullPointerException ignored) {}
		
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
			
		eb.setAuthor("Description");
		//EMBED COLOR
		eb.setColor(Color.decode(hexCode));
		
			
		//FIELD DESCRIPTION
		if(des == null) {
			eb.setDescription("There is no description provided for this command.");
		}else{
			eb.setDescription(des.substring(0, 1).toUpperCase() + des.substring(1));
		}
		
		if(argu != null) {
			boolean field = false;
			if((ConfigUtil.getPrefix()+cmd+" "+argu[0]).length() < 25) {
				field = true;
			}
			eb.addField("Usage",ConfigUtil.getPrefix()+cmd + " " + argu[0], field);
			eb.addField("Example",ConfigUtil.getPrefix()+cmd + " " + argu[1].substring(1), field);
			try {
				eb.setImage(argu[2]);
			}catch(ArrayIndexOutOfBoundsException ignored) {}
		}
		
		String aliase = null;
		if(Objects.requireNonNull(alias).length != 0) {
			StringBuilder hm = new StringBuilder();
			for (String anAliaslist : alias) {
				aliase = hm.append(anAliaslist).append(", ").toString();
			}
			eb.addField("Aliases", aliase.substring(0, Objects.requireNonNull(aliase).length() - 2), false);
		}
		
		eb.setFooter("Help command of '" + ConfigUtil.getPrefix()+cmd+"'", null);
		mb.setEmbed(eb.build());
		return mb.build();
	}
}
