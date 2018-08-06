package commands.music;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.OtherUtil;
import utility.audio.MusicManager;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;

public class GetPlaylistCmd extends Music {

	public GetPlaylistCmd() {
		this.name = MUSIC_PREFIX+"getqueue";
		this.aliases = new String[] {MUSIC_PREFIX+"getq"};
		this.help = "Have the bot send you a link full of the songs in the queue :)";
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		List<QueuedTrack> queue = ms.getGuildAudioPlayer(e.getGuild()).scheduler.getQueue().getList();    

		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There is no track playing right now", e.getChannel());
			return;
		}
		
		if(queue.isEmpty()) {
			UsrMsgUtil.sendEMessage("There are no tracks in the queue", e.getChannel());
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (QueuedTrack aQueue : queue) {
			sb.append(aQueue.getTrack().getInfo().uri).append("\n");
		}
		UsrMsgUtil.sendVEMessage("Here's the playlist queue links!\n"+OtherUtil.postToHaste(sb.toString()), e.getChannel());
	}
}
