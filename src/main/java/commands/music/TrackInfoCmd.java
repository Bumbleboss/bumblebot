package commands.music;

import java.awt.Color;
import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import utility.ConfigUtil;
import utility.audio.GuildMusicManager;
import utility.audio.MusicManager;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;

public class TrackInfoCmd extends Music {

	public TrackInfoCmd() {
		this.name = MUSIC_PREFIX+"trackinfo";
		this.help = "Check the info about the given track";
		this.arguments = "<query|index> {} Kozah";
		this.aliases = new String[] {MUSIC_PREFIX+"ti"};
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		GuildMusicManager musicManager = ms.getGuildAudioPlayer(e.getGuild());
	    List<QueuedTrack> queue = musicManager.scheduler.getQueue().getList();
		
		if(queue.isEmpty()) {
			if(ms.isPlaying(e.getGuild())) {
				UsrMsgUtil.sendEMessage("There is no track playing right now", e.getChannel());
	    		return;
	    	}
			UsrMsgUtil.sendEMessage("There are no tracks in the queue to retrieve info", e.getChannel());
			return;
	    }
		
		boolean isInt = true;
		int num = 0;
		try {
			num = Integer.parseInt(e.getArgs());
		}catch(NumberFormatException ex) {
			isInt = false;
		}
			    
	    if(!isInt) {
	    	for(int i = 0; i < queue.size(); i++) {
	    		if(queue.get(i).getTrack().getInfo().title.toLowerCase().contains(e.getArgs().toLowerCase())) {
	    			e.reply(getMessage(i, queue));
	    		    return;
	    		}
	    	}
			UsrMsgUtil.sendEMessage("No results were found with the query of **" +e.getArgs()+"**", e.getChannel());
			return;
	    }
	    
	    if(num < 1 || num > queue.size()) {
			UsrMsgUtil.sendEMessage("Position must be between **1** and **" + queue.size()+"**", e.getChannel());
			return;
		}
	    e.reply(getMessage(num-1, queue));
	}
	
	public MessageEmbed getMessage(int i, List<QueuedTrack> queue) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(ConfigUtil.getHex()));
		QueuedTrack tr = queue.get(i);
		AudioTrack trr = tr.getTrack();
		
		String previous = null;
		try {
			previous = queue.get(i-1).toString(false, true, 40);
		}catch (IndexOutOfBoundsException ex) {}
		
		String after = null;
		try {
			after = queue.get(i+1).toString(false, true, 40);
		}catch (IndexOutOfBoundsException ex) {}
			
		
		eb.addField("Position in queue", i+1 + "", true);
		eb.addField("Requester", UsrMsgUtil.getUserSet(Bumblebot.jda, tr.getOwner().getId()), true);
		eb.addField("Length", ms.formatTime(trr.getDuration()), true);
		eb.addField("Track order",(previous==null?"":previous)+"\n**>** "+ tr.toString(false, true, 40) + "\n" + (after==null?"":after), false);
		eb.setFooter("Uploaded by " + trr.getInfo().author + " on " + trr.getSourceManager().getSourceName(), null);
		return eb.build();
	}
}
