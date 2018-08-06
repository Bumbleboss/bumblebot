package commands.api;

import java.awt.Color;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utility.ConfigUtil;
import utility.OtherUtil;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class ChatterBot extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		String url = "http://127.0.0.1:5000/get?msg=";
		String botid = e.getJDA().getSelfUser().getId();
		
		if(e.getAuthor().isBot()) {
			return;
		}
		
		try {
			if(e.getGuild().getId().equals(ConfigUtil.getServerId()) && e.getChannel().getId().equals(ConfigUtil.getCleverTC())) {
				if(e.getMessage().getAttachments().size() > 0) {
					String[] replies = new String[] {"Hey, I don't reply to images!", "No images pls ;-;", "I don't respond to images >.>", "Please refrain from sending images here, I am learning..."};
		            e.getChannel().sendMessage(replies[(int)(Math.random()*replies.length)]);
					return;
				}
				e.getChannel().sendMessage(e.getAuthor().getName() + ", " + OtherUtil.getGET(url+e.getMessage().getContentDisplay())).queue();
				
			}else if(e.getMessage().getContentRaw().startsWith("<@!"+botid+"> ")) {
				String response = e.getMessage().getContentDisplay().replace("<@!"+botid+"> ", "");
				e.getChannel().sendMessage(e.getAuthor().getName() + ", " + OtherUtil.getGET(url+response)).queue();
				
			}else if(e.getMessage().getContentRaw().startsWith("<@"+botid+"> ")) {
				String response = e.getMessage().getContentDisplay().replace("<@"+botid+"> ", "");
				e.getChannel().sendMessage(e.getAuthor().getName() + ", " + OtherUtil.getGET(url+response)).queue();
				
			}else if(e.getMessage().getContentRaw().startsWith(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" "+"<@!"+botid+">")
					|| e.getMessage().getContentRaw().startsWith(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" "+"<@"+botid+">")) {
				e.getChannel().sendMessage(new EmbedBuilder().setAuthor("Description")
						.setDescription("Chat with me and I will be sure to respond to you with the nicest way possible!")
						.setColor(Color.decode(ConfigUtil.getHex()))
						.setFooter("help command of '@" + Bumblebot.jda.getSelfUser().getName()+"'", null)
						.build()).queue();
			}
		}catch (Exception ex) {
			if(ex instanceof IllegalArgumentException || ex instanceof NullPointerException) {
				e.getChannel().sendMessage(e.getAuthor().getName() + ".. Sorry, I didn't catch that!").queue();
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
