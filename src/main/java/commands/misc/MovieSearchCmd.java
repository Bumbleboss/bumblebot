package commands.misc;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.Movie;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class MovieSearchCmd extends Command {

	public MovieSearchCmd() {
		this.name = "movie";
		this.help = "Search for a movie on TMDB";
		this.arguments = "<movie> {} Interstellar";
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
			int id = api.searchService().movie(e.getArgs(), null, null, true, null, null, null).execute().body().results.get(0).id;
			Movie mv = api.moviesService().summary(id).execute().body();
			EmbedBuilder eb = new EmbedBuilder();
				
			eb.setAuthor("TMDB", "https://www.themoviedb.org/movie/"+id, "https://i.imgur.com/G9q4DF1.png");
			eb.setColor(Color.decode("#00D474"));
			eb.setDescription("["+mv.title+"](https://www.themoviedb.org/movie/"+id+")\n"+
					(mv.overview==null? "N/A" : (mv.overview.length()>1024? mv.overview.substring(0, 1000)+"..." : mv.overview)));
			eb.setThumbnail("https://image.tmdb.org/t/p/w500"+mv.poster_path);
			eb.addField("Release date", mv.release_date==null? "N/A" : OtherUtil.getDate(mv.release_date.toString()), true);
			eb.addField("Rating", mv.rating==null? "N/A" : mv.rating + "%", true);
			eb.addField("Budget", mv.budget==null? "N/A": OtherUtil.getCount(mv.budget.toString()) +"$", true);
			eb.addField("Revenue", mv.revenue==null? "N/A": OtherUtil.getCount(mv.revenue.toString()) +"$", true);
			eb.addField("Duration", mv.runtime==null? "N/A": mv.runtime + " mins", true);
				
			String genres = null;
			if(mv.genres==null) {
				genres = "N/A";
			}else{
				StringBuilder gen = new StringBuilder();
				for(int i = 0; i < 3 && i < mv.genres.size(); i++) {
					gen.append(mv.genres.get(i).name.substring(0, 1).toUpperCase())
					.append(mv.genres.get(i).name.substring(1, mv.genres.get(i).name.length())+", ");
				}
				genres = gen.toString().substring(0, gen.length()-2)+".";
			}
				
			eb.addField("Genres", genres, true);
			e.reply(eb.build());
		} catch (Exception ex) {
			if(ex instanceof IndexOutOfBoundsException) {
				UsrMsgUtil.sendEMessage("Movie was not found! ;-;", e.getChannel());
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}	
	}
}
