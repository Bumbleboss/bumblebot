package commands.misc;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import musixMatchAPI.MusixMatch;
import musixMatchAPI.MusixMatchException;
import musixMatchAPI.entity.lyrics.Lyrics;
import musixMatchAPI.entity.track.Track;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class LyricsCmd extends Command {

	public LyricsCmd() {
		this.name = "lyrics";
		this.aliases = new String[] {"lyrc", "ly"};
		this.arguments = "<artist>;<track> {} Alan Walker;Faded";
		this.help = "Search lyrics of a track!";
		this.category = Bumblebot.Misc;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		EmbedBuilder eb = new EmbedBuilder();
		MusixMatch api = ConfigUtil.lyrc;
		
		if(e.getArgs().isEmpty()) {
			UsrMsgUtil.sendEMessage("You need to provide something!", e.getChannel());
			return;
		}
		
		try {
			String[] args = e.getArgs().split(";");
			Track track = api.getMatchingTrack(args[0], args[1]);
			Lyrics lyrc = api.getLyrics(track.getTrack().getTrackId());
			
			eb.setColor(Color.decode("#FF6050"));
			eb.setAuthor("Lyrics");
			eb.setDescription(lyrc.getLyricsBody().replace("******* This Lyrics is NOT for Commercial use *******\n(1409615473592)", "")
					+ "["+ track.getTrack().getArtistName() + " - " + track.getTrack().getTrackName()+"]"
					+ "("+track.getTrack().getTrackShareUrl().substring(0, track.getTrack().getTrackShareUrl().lastIndexOf("?"))+")");
			eb.setFooter("Provided by MusixMatch", "http://brand.musixmatch.com/img/528cf00b303e3d89700006c4/617089/upload-40e9b1b0-9523-11e6-adda-0fb2759a0a66.png");
			e.reply(eb.build());
		} catch (Exception ex) {
			if(ex instanceof MusixMatchException || ex instanceof IndexOutOfBoundsException){ 
				UsrMsgUtil.sendEMessage("Lyrics not found! ;-;", e.getChannel());
			}else {
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}