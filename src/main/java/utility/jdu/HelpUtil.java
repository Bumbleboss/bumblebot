package utility.jdu;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
	
    private final LinkedList<Command> command1 = new LinkedList<>();
	
	public void onMessageReceived(MessageReceivedEvent s) {
		StringBuilder category = new StringBuilder();
		String cat = null;
		for(int i = 0; i < command1.size(); i++) {			
			if(command1.get(i).getCategory().equals(Bumblebot.myServer)) {
				if(s.getChannelType().equals(ChannelType.PRIVATE)) {
					continue;
				}else{
					if(!s.getGuild().getId().equals(ConfigUtil.getServerId())) {
						continue;
					}
				}
			}
			
			if(command1.get(i).getCategory().equals(Bumblebot.Mod)) {
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
			
			if(command1.get(i).isHidden() && !OtherUtil.isOwners(s)) {
				continue;
			}
			
			if(command1.get(i).isOwnerCommand() && !OtherUtil.isOwners(s)) {
				continue;
			}
		
			if(s.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" "+ getHelp().get(i).getName())) {
				s.getChannel().sendMessage(getHelpMessage(i)).queue();
			}else{ 
				List<String> lsts = Arrays.asList(getHelp().get(i).getAliases());
				for(int i2 = 0; i2 < lsts.size(); i2++) {
					if(s.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" "+ lsts.get(i2).toString())) {
						s.getChannel().sendMessage(getHelpMessage(i)).queue();
					}
				}
			} 
			
			if(s.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" " + command1.get(i).getCategory().getName())) {				
				cat = command1.get(i).getCategory().getName();
				category.append(getCategoryMessage(command1.get(i).getCategory(), i));
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
			String helpDescription = null;
			if(getHelp().get(i).getHelp().length() > 30) {
				helpDescription = getHelp().get(i).getHelp().substring(0, 30).replace("*", "").replace("\n", " ") + "...";
			}else{
				helpDescription = getHelp().get(i).getHelp().replace("*", "").replace("\n", "");
			}
			return "**"+ getHelp().get(i).getName() + "** - *" + helpDescription+"*\n";
		}
		
		return null;
	}
	
	private LinkedList<Command> getHelp() { 
		return this.command1;
	}
	
	public void setHelpCommands(Command... helpcmd) {
		for(Command command: helpcmd)
			command1.add(command);
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
		}catch (NullPointerException e) {}
		//COMMAND ALIASES
		
		String[] alias = null;
		try {
			alias = help.getAliases();
		}catch (NullPointerException e) {}
		
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
		
		if(argu == null) {
		}else{
			boolean field = false;
			if((ConfigUtil.getPrefix()+cmd+" "+argu[0]).length() < 25) {
				field = true;
			}
			eb.addField("Usage",ConfigUtil.getPrefix()+cmd + " " + argu[0], field);
			eb.addField("Example",ConfigUtil.getPrefix()+cmd + " " + argu[1].substring(1), field);
			try {
				eb.setImage(argu[2]);
			}catch(ArrayIndexOutOfBoundsException ex) {}
		}
		
		String aliase = null;
		if(alias.length == 0) {
		}else{
			StringBuilder hm = new StringBuilder();
			List<String> aliaslist = Arrays.asList(alias);
			for(int i1 = 0; i1 < aliaslist.size(); i1++) {
				aliase =	hm.append(aliaslist.get(i1) + ", ").toString();
			}
			eb.addField("Aliases", aliase.substring(0, aliase.length() - 2), false);
		}
		
		eb.setFooter("Help command of '" + ConfigUtil.getPrefix()+cmd+"'", null);
		mb.setEmbed(eb.build());
		Message m = mb.build();
		return m;	
	}
}
