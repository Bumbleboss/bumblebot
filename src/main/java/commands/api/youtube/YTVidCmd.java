package commands.api.youtube;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import youTubeAPI.YouTubeAPI;
import youTubeAPI.core.entities.search.Item;
import youTubeAPI.core.exceptions.YouTubeException;

public class YTVidCmd extends Command{

	
	public YTVidCmd() {
		this.name = "ytvid";
		this.help = "Searches for YouTube videos";
		this.aliases = new String[] {"vid"};
		this.arguments = "<query> {} Kozah - Dream state";
		this.category = Bumblebot.API;
	}

	@Override
	protected void execute(CommandEvent e) {
		if(e.getArgs().isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}
		
		YouTubeAPI inf = ConfigUtil.yt;	
		EmbedBuilder eb = new EmbedBuilder();
		
		try {
			Item srch = inf.searchVideo(e.getArgs()).getItems().get(0);
			eb.setAuthor("YouTube", "https://www.youtube.com/", "http://i.imgur.com/hkUafwu.png");
			eb.setDescription("["+srch.getInfo().title + "](https://youtu.be/"+srch.getId().videoId+")");
			eb.setImage("http://img.youtube.com/vi/"+srch.getId().videoId+"/mqdefault.jpg");
			eb.setColor(Color.decode("#DD2825"));
			eb.setFooter("Published by " + srch.getInfo().channelTitle + " on " + OtherUtil.getDate(srch.getInfo().publishedAt), null);
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
