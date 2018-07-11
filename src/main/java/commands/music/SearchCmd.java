package commands.music;

import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.core.EmbedBuilder;
import utility.audio.MusicManager;
import utility.jdu.OrderMenu;

public class SearchCmd extends Music{

	private final OrderMenu.Builder builder;
	public SearchCmd(EventWaiter waiter) {
		this.name = MUSIC_PREFIX + "search";
		this.arguments = "<query> {} Kozah - Dream State";
		this.aliases = new String[] {MUSIC_PREFIX+"srch", MUSIC_PREFIX+"yt"};
		this.help = "Searches for track on YT and returns a list of options";
		builder = new OrderMenu.Builder()
	        		.allowTextInput(true)
	        		.useNumbers()
	        		.setEventWaiter(waiter)
	        		.setTimeout(1, TimeUnit.MINUTES);
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(!e.getSelfMember().getVoiceState().inVoiceChannel()) {
			ms.connectToVc(e.getGuild().getAudioManager(), e.getMember());
		}
		e.reply(new EmbedBuilder().setDescription("Searching for... " + e.getArgs()).build(), m -> {
			ms.loadTracks(e.getTextChannel(), "ytsearch: " + e.getArgs(), e.getAuthor(), builder, m);
		});
	}
}
