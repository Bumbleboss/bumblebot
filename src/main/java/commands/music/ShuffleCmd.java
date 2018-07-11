package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class ShuffleCmd extends Music{

	public ShuffleCmd() {
		this.name = MUSIC_PREFIX+"shuffle";
		this.help = "Shuffles the tracks that you've added in the queue";
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There are no tracks playing", e.getChannel());
			return;
		}
		int s =	ms.getGuildAudioPlayer(e.getGuild()).scheduler.getQueue().shuffle(e.getAuthor().getIdLong());
		switch(s) {
			case 0: 
				UsrMsgUtil.sendEMessage("You don't have any tracks in the queue", e.getChannel());
				break;
			case 1:
				UsrMsgUtil.sendEMessage("You only have a track in the queue", e.getChannel());
				break;
			default:
				UsrMsgUtil.sendVEMessage("Shuffled **"+s+"** tracks from your entries" , e.getChannel());
		}
	}
}
