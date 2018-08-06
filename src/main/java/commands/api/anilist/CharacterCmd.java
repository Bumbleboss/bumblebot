package commands.api.anilist;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import aniListAPI.AniListInfo;
import aniListAPI.entities.AniListCharacterData;
import aniListAPI.entities.exceptions.AniListException;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.jdu.OrderMenu;

public class CharacterCmd extends Command {
	
	private final OrderMenu.Builder builder;
	public CharacterCmd(EventWaiter waiter) {
		this.name = "character";
		this.arguments = "<character> {} Yuuki Asuna";
		this.help = "Search for an anime character from AniList.";
        this.category = Bumblebot.API;
        builder = new OrderMenu.Builder()
        		.allowTextInput(true)
        		.useNumbers()
        		.setTimeout(1, TimeUnit.MINUTES)
        		.setEventWaiter(waiter);
	}

	@SuppressWarnings("RedundantArrayCreation")
	@Override
	protected void execute(CommandEvent e) {
		String args = e.getArgs();
		if(args.isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}
		
		e.reply(new EmbedBuilder().setDescription("Searching for... "+args).build(), m->{	
			try {
				List<AniListCharacterData> charr = new AniListInfo().getCharacter(args);		
				if(charr.size() == 1) {
					m.editMessage(getCharacter(args, 0)).queue();
				}else{
					builder.setColor(Color.decode(ConfigUtil.getHex()))
					.setChoices(new String[0])
					.setDescription("Results")
					.setSelection((msg, i) -> {
						try {
							e.reply(getCharacter(args, i-1));
						} catch (AniListException e1) {
							e.reply(new EmbedBuilder().setDescription("No result found on the requested anime character! ;-;").build());
						}
					})
					.setCancel((msg) -> e.reply(new EmbedBuilder().setDescription("Selection has been canceled.").build()))
					.setUsers(e.getAuthor());
					for(int i = 0; i < 3 && i < charr.size(); i++) {
						AniListCharacterData chars = charr.get(i);
						String japs = chars.getNameJapanese()==null? "" : " | " + chars.getNameJapanese();
						builder.addChoices("["+chars.getNameFirst() +" "+chars.getNameLast() 
						+ japs + "](https://anilist.co/character/"+chars.getId()+")");
					}
					builder.build().display(m);
				}
			}catch (Exception ex) {
				if(ex instanceof AniListException) {
					m.editMessage(new EmbedBuilder().setDescription("No result found on the requested anime character! ;-;").build()).queue();
					return;
				}
				m.editMessage(new EmbedBuilder().setDescription("Bot was unable to retrieve info from AniList API.").build()).queue();
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		});	
	}
	
	private Message getCharacter(String query, Integer i) throws AniListException{
		MessageBuilder mb = new MessageBuilder(); 
		List<AniListCharacterData> charr = new AniListInfo().getCharacter(query);
		EmbedBuilder eb = new EmbedBuilder();
		AniListCharacterData chrr = charr.get(i);
			
		eb.setColor(Color.decode(ConfigUtil.getHex()));
		eb.setAuthor(chrr.getNameFirst(), "https://anilist.co/character/"+chrr.getId(),chrr.getMediumImageUrl());
		eb.addField("Alternative names", chrr.getNameAlt()==null? "N/A": chrr.getNameAlt(), true);				
		eb.addField("Japanese name", chrr.getNameJapanese()==null? "N/A": chrr.getNameJapanese(), true);
			
		String abt = chrr.getInfo()==null?"N/A":chrr.getInfo().replace("<i>", "*").replace("</i>","*").replace("<br>", "").replace("_", "").replace("<b>", "**").replace("</b>", "**");			
		eb.setDescription(abt.length() > 960?abt.substring(900) + "... [Read more]("+"https://anilist.co/character/"+chrr.getId()+")":abt);
			
		eb.setThumbnail(chrr.getLargeImageUrl());
		eb.setFooter("Provided by AniList.", null);
		mb.setEmbed(eb.build());
			
		return mb.build();
	}
}
