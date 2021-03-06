package commands.myServer;

import main.Bumblebot;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class AntiLink extends ListenerAdapter{

	private final String inviteRegex = "di?sco?rd(?:(\\.(?:me|io|gg|li)|sites\\.com).{0,4}|app\\.com.{1,4}(?:invite|oauth2).{0,5})\\w+";

	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if(e.getGuild().getIdLong() == ConfigUtil.getServerId()) {
			try {
				if(!e.getMember().hasPermission(Bumblebot.modPerms)) {
					if (e.getMessage().getContentRaw().replaceAll(this.inviteRegex, "").length() < e.getMessage().getContentRaw().length()) {
						e.getChannel().deleteMessageById(e.getMessage().getIdLong()).queue();
						UsrMsgUtil.sendEMessage("Invites are not allowed! " + e.getAuthor().getAsMention(), e.getChannel());
					}
				}
			}catch (NullPointerException ignored) {}
		}
	}
	
	public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
		if(e.getGuild().getIdLong() == ConfigUtil.getServerId()) {
			try {
				if(!e.getMember().hasPermission(Bumblebot.modPerms)) {
					if(e.getMessage().getContentRaw().replaceAll(this.inviteRegex, "").length() < e.getMessage().getContentRaw().length()) {
						UsrMsgUtil.sendEMessage("Invites are not allowed! " + e.getAuthor().getAsMention() + " ... and nice try :D", e.getChannel());
						e.getChannel().deleteMessageById(e.getMessage().getIdLong()).queue();
					}
				}
			}catch (NullPointerException ignored) {}
		}
	}
}
