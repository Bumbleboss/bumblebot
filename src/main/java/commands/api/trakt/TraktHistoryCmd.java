package commands.api.trakt;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.HistoryEntry;
import com.uwetrottmann.trakt5.entities.User;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.enums.Extended;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;
import utility.jdu.OrderMenu;

public class TraktHistoryCmd extends Command {

	private final OrderMenu.Builder bd;
	public TraktHistoryCmd(EventWaiter waiter) {
		this.name = "trakthistory";
		this.aliases = new String[] {"trhis"};
		this.help = "Get watched history of a Trakt.tv user!";
		this.arguments = "<username> {} Bumbleboss";
		this.category = Bumblebot.API;
		bd = new OrderMenu.Builder()
				.allowTextInput(true)
				.setTimeout(1, TimeUnit.MINUTES)
				.setEventWaiter(waiter);
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String args = e.getArgs();
		if(args.isEmpty()) {
			UsrMsgUtil.sendEMessage("You need to provide a username!", e.getChannel());
			return;
		}
		
		TraktV2 trakt = ConfigUtil.trakt;
		UserSlug usr = new UserSlug(args);
		
		e.reply(new EmbedBuilder().setDescription("Looking up history of... **"+args+"**").build(),m -> {
			try {
				User user = trakt.users().profile(usr, Extended.FULL).execute().body();
				if(user.isPrivate) {
					m.editMessage(new EmbedBuilder().setDescription("User **" + args + "** is private!").build()).queue();
					return;
				}
				
				List<HistoryEntry> cmnt = trakt.users().history(usr, 0, 10, Extended.FULL, null, null).execute().body();
				if(cmnt.size() == 0) {
					try {
						m.editMessage(getHistory(0, cmnt)).queue();
					}catch (IndexOutOfBoundsException ex) {
						m.editMessage(new EmbedBuilder().setDescription("User **" + args + "** has no history!").build()).queue();
					}
				}else{
					bd.setColor(Color.decode("#ED1C24"))
					.setChoices(new String[0])
					.setDescription("Results")
					.setSelection((msg, i)-> {e.reply(getHistory(i-1, cmnt));})
					.setCancel((msg) -> {e.reply(new EmbedBuilder().setDescription("You have canceled the selection.").build());})
					.setUsers(e.getAuthor());
					for(int i = 0; i < 5 && i < cmnt.size(); i++) {
						HistoryEntry list = cmnt.get(i);
							
						StringBuilder eb = new StringBuilder();
						if(list.type.equals("show")) {
							eb.append("["+list.show.title.toString()+"](https://trakt.tv/shows/"+list.show.ids.trakt+")");
						}
						if(list.type.equals("episode")) {
							eb.append("["+list.show.title + ": Season "+ list.episode.season 
									+ " [Episode " + list.episode.number+"]"
									+ "](https://trakt.tv/shows/"+list.show.ids.trakt+"/seasons/"+list.episode.season+"/episodes/"+list.episode.number+")");
						}
						if(list.type.equals("movie")) {
							eb.append("["+list.movie.title.toString()+"](https://trakt.tv/movies/"+list.movie.ids.trakt+")");
						}
						bd.addChoices(eb.toString());
					}
					bd.build().display(m);
				}
			} catch (Exception ex) {
				if(ex instanceof NullPointerException) {
					m.editMessage(new EmbedBuilder().setDescription("User **" + args + "** was not found.").build()).queue();
				}else{
					m.editMessage(new EmbedBuilder().setDescription("Error while doing something").build()).queue();
					OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
				}
			}
		});
	}
	
	private Message getHistory(Integer i, List<HistoryEntry> cmnt) {
		EmbedBuilder eb = new EmbedBuilder();
		StringBuilder sb = new StringBuilder();
		HistoryEntry his = cmnt.get(i);
		
		eb.setColor(Color.decode("#ED1C24"));
		if(his.type.equals("show")) {
			eb.setAuthor(his.show.title, "https://trakt.tv/shows/"+his.show.ids.trakt);
			sb.append(his.show.overview == null ? "N/A" : his.show.overview);
		}else if(his.type.equals("episode")){
			sb.append(his.episode.overview == null ? "N/A" : (his.episode.overview.length() > 1024 ? his.episode.overview.substring(0, 1000) + " ..." : his.episode.overview));
			eb.addField("Episode", his.episode.season+"x"+his.episode.number +" " + his.episode.title, true);
			eb.addField("Aired", OtherUtil.getDate(his.episode.first_aired.toString()) + " ", true);
		}else if(his.type.equals("movie")) {
			eb.setAuthor(his.movie.title, "https://trakt.tv/movies/"+his.movie.ids.trakt);
			eb.addField("Runtime", his.movie.runtime + " mins", true);
			eb.addField("Released", OtherUtil.getDate(his.movie.released.toString()) + " ", true);
			sb.append(his.movie.overview == null ? "N/A" : (his.movie.overview.length() > 1024 ? his.episode.overview.substring(0, 1000) + " ...": his.movie.overview));
		}
		eb.addField("About the " + his.type, sb.toString(), false);
		return new MessageBuilder().setEmbed(eb.build()).build();
	}
}
