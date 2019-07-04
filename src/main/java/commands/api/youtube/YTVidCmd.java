package commands.api.youtube;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;
import youTubeAPI.YouTubeAPI;
import youTubeAPI.core.entities.search.Item;
import youTubeAPI.core.error.YouTubeException;

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
			e.reply("https://youtu.be/"+srch.getId());
		} catch (Exception ex) {
			if(ex instanceof YouTubeException) {
				UsrMsgUtil.sendEMessage("No results found! ;-;", e.getChannel());
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}	
}
