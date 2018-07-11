package commands.myServer.rules;

import java.awt.Color;
import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;

import commands.myServer.Server;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class RulesCmd extends Server {

	public RulesCmd() {
		this.name = "rules";
		this.help = "Check all the info you need of each channel!";
		this.arguments = "<channel_name> {} nsfw";
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		String chnls = null;
		if(e.getMessage().getMentionedChannels().size() > 0) {
			chnls = e.getMessage().getMentionedChannels().get(0).getName();
		}
		
		String chnl = chnls==null?e.getArgs():chnls;
		String chnlName = null;
		List<Object> list = RulesManager.getChannels().toList();
		for(int i = 0; i < list.size(); i++) {
			if(RulesManager.getChannelName(i).equalsIgnoreCase(chnl)) {
				chnlName = RulesManager.getChannelName(i);
			}else if(RulesManager.getChannelId(i).equalsIgnoreCase(chnl)) {
				chnlName = RulesManager.getChannelName(i);
			}
		}
			
		if(chnlName == null) {
			chnlName = chnl;
		}
			
		EmbedBuilder eb = new EmbedBuilder();
		if(chnl.isEmpty()) {
			eb.setAuthor("Server rules", null, e.getGuild().getIconUrl());
			eb.setDescription("**Rule #1:** *Spamming is only tolerated in* ***#dumb***."
					+ "\n**Rule #2:** *Advertising Discord servers is not allowed.*"
					+ "\n**Rule #3:** *Please be nice! That's what I am asking for!*"
					+ "\n**Rule #4:** *NSFW content are prohibted in normal chats.*"
					+ "\n**Rule #5:** *Do not annoy any of the admins on silly topics.*"
					+ "\n**Rule #6:** *You must abide the rules noted in Discord's community guidelines.*");
			eb.setFooter("Mentioned by "+UsrMsgUtil.getUserSet(e.getJDA(), e.getAuthor().getId())+" | Thanks!", null);
		}else if(RulesManager.matchChannel(chnlName)) {
			if(RulesManager.getChannelEmbed(chnlName).equals("null")) {
				eb.addField("Description", RulesManager.getChannelDesc(chnlName), false);
			}else{
				eb.addField("Rules", RulesManager.getChannelDesc(chnlName), false);
				eb.addField("HowTo", RulesManager.getChannelEmbed(chnlName), false);
			}
			eb.setFooter("Mentioned by "+UsrMsgUtil.getUserSet(e.getJDA(), e.getAuthor().getId())+" | Thanks!", null);
		}else{
			eb.setDescription("There are no info for any of the given channel name/Id");
		}
		e.reply(eb.setColor(Color.decode(ConfigUtil.getHex())).build());
	}
}
