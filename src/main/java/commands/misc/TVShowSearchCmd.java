package commands.misc;

import java.awt.Color;
import java.util.Objects;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.TvShow;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class TVShowSearchCmd extends Command {

	public TVShowSearchCmd() {
		this.name = "tvshow";
		this.help = "Search for a TV show on TMDB";
		this.arguments = "<tvshow> {} Stranger Things";
		this.category = Bumblebot.Misc;
	}
	@Override
	protected void execute(CommandEvent e) {
		if(e.getArgs().isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}

		Tmdb api = ConfigUtil.tmdb;
		try {
			int id = Objects.requireNonNull(api.searchService().tv(e.getArgs(), null, null, null, null).execute().body()).results.get(0).id;
			TvShow tv = api.tvService().tv(id, null).execute().body();
			EmbedBuilder eb = new EmbedBuilder();
				
			eb.setAuthor("TMDB", "https://www.themoviedb.org/tv/"+id, "https://i.imgur.com/G9q4DF1.png");
			eb.setColor(Color.decode("#00D474"));
			eb.setDescription("["+Objects.requireNonNull(tv).name+"](https://www.themoviedb.org/tv/"+id+")\n"+
					(tv.overview==null? "N/A" : (tv.overview.length()>1024? tv.overview.substring(0, 1000) : tv.overview)));
			eb.setThumbnail("https://image.tmdb.org/t/p/w500"+tv.poster_path);
			eb.addField("Release date", tv.first_air_date==null? "N/A" : OtherUtil.getDate(tv.first_air_date.toString()), true);
			eb.addField("Rating", tv.rating==null? "N/A" : tv.rating + "%", true);
			eb.addField("Seasons | Episodes", 
					(tv.number_of_seasons==null? "N/A" : tv.number_of_seasons) + " | " + (tv.number_of_episodes==null? "N/A" : tv.number_of_episodes), true);
			eb.addField("Genres", OtherUtil.getGenreString(tv.genres), true);
			e.reply(eb.build());
		} catch (Exception ex) {
			if(ex instanceof IndexOutOfBoundsException) {
				UsrMsgUtil.sendEMessage("TV Show was not found! ;-;", e.getChannel());
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
