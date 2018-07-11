package commands.api.anilist;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import aniListAPI.AniListInfo;
import aniListAPI.entities.AniListAnimeData;
import aniListAPI.entities.exceptions.AniListException;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.jdu.OrderMenu;

public class AnimeCmd extends Command{

	private final OrderMenu.Builder builder;
	public AnimeCmd(EventWaiter waiter) {
		this.name = "anime";
		this.arguments = "<anime> {} Sword Art Online";
		this.help = "Searches for an Anime series or movie through AniList";
        this.category = Bumblebot.API;
        builder = new OrderMenu.Builder()
        		.allowTextInput(true)
        		.useNumbers()
        		.setEventWaiter(waiter)
        		.setTimeout(1, TimeUnit.MINUTES);
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String args = e.getArgs();
		if(args.isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}
		
		e.reply(new EmbedBuilder().setDescription("Searching for... "+args).build(),m -> {
			try {				
				List<AniListAnimeData> anim = new AniListInfo().getAnimeSeries(args);
				if(anim.size() == 1) {	
					m.editMessage(getAnime(args, 0)).queue();
				}else{
					builder.setColor(Color.decode(ConfigUtil.getHex()))
					.setChoices(new String[0])
					.setDescription("Results")	
					.setSelection((msg, i) ->  {
						try {
							e.reply(getAnime(args, i-1));
						} catch (AniListException e1) {
							e.reply(new EmbedBuilder().setDescription("No results found! ;-;").build());
						}
					})
					.setCancel((msg) -> {
						e.reply(new EmbedBuilder().setDescription("Selection has been canceled.").build());
					})
					.setUsers(e.getAuthor());
					for(int i=0; i < 3 && i < anim.size(); i++) {
						AniListAnimeData list = anim.get(i);
						builder.addChoices("["+list.getTitleEnglish() + "](https://anilist.co/"+list.getType()+"/"+list.getId()+")");
					}
					builder.build().display(m);
				}
			} catch (Exception ex) {
				m.editMessage(new EmbedBuilder().setDescription("Bot was unable to retrieve info from AniList API.").build()).queue();
			}
		});
	}
	
	public Message getAnime(String query, Integer i) throws AniListException {
		MessageBuilder mb = new MessageBuilder();
		AniListAnimeData ani = new AniListInfo().getAnimeSeries(query).get(i);
		EmbedBuilder eb = new EmbedBuilder();
			
		eb.setColor(Color.decode(ConfigUtil.getHex()));
		eb.setAuthor(ani.getTitleEnglish(),"https://anilist.co/anime/"+ani.getId(), ani.getSmallImageUrl());
			
		String des = ani.getDescription().replace("<i>", "*").replace("</i>","*").replace("<br>", "").replace("_", "").replace("<b>", "**").replace("</b>", "**");
		eb.setDescription(des.length() > 960?des.substring(900) + "... [Read more]("+"https://anilist.co/anime/"+ani.getId()+")":des);
			
		eb.addField("Start date", ani.getStartDate()==null?"N/A":OtherUtil.getDate(ani.getStartDate()), true);
		eb.addField("End date", ani.getEndDate()==null?"N/A":OtherUtil.getDate(ani.getEndDate()), true);
		eb.addField("Type", ani.getType(), true);
		eb.addField("Score", ani.getMeanScore() + "%", true);
		eb.setImage(ani.getBannerUrl());
		eb.setFooter("Provided by AniList.", null);
		mb.setEmbed(eb.build());
			
		return mb.build();
	}
}
