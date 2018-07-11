package commands.api.youtube;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import youTubeAPI.YouTubeAPI;
import youTubeAPI.core.entities.channels.ChannelItems;
import youTubeAPI.core.entities.search.Item;
import youTubeAPI.core.exceptions.YouTubeException;

public class YTChnlCmd extends Command {
	
	public YTChnlCmd() {
		this.name = "ytchnl";
		this.help = "Search for YouTube channels";
		this.aliases = new String[] {"chnl"};
		this.arguments = "<query> {} BumbleCore";
		this.category = Bumblebot.API;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		if(e.getArgs().isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		YouTubeAPI inf = ConfigUtil.yt;
		
		try {
			Item srch = inf.searchChannel(e.getArgs()).getItems().get(0);
			String id = srch.getId().channelId;
			ChannelItems cnl = inf.getChannel(id).getItems().get(0);
			
			eb.setAuthor(cnl.getInfo().title, "https://www.youtube.com/channel/"+cnl.id, cnl.getInfo().getThumbnails().getHigh().url);
			
			String des = srch.getInfo().description;
			eb.addField("About", des.isEmpty()?"N/A":des, false);
			
			eb.setThumbnail(cnl.getInfo().getThumbnails().getHigh().url);
			eb.setColor(Color.decode("#DD2825"));
			
			if(!cnl.getStatistics().hiddenSubscriberCount) {
				eb.setFooter(OtherUtil.getCount(cnl.getStatistics().subscriberCount) + " Subs | "
						   + OtherUtil.getCount(cnl.getStatistics().viewCount) +" Views | "
						   + OtherUtil.getCount(cnl.getStatistics().videoCount) + " Videos", null);
			}
			e.reply(eb.build());			
		} catch (Exception ex) {
			if(ex instanceof YouTubeException) {
				e.reply("No videos were found! ;-;");
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
