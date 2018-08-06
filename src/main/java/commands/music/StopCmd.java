package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class StopCmd extends Music{
	
	public StopCmd() {
		this.name = MUSIC_PREFIX+"stop";
		this.help = "Stops what's currently playing and clears the queue.";
		this.aliases = new String[] {MUSIC_PREFIX+"stp"};
	}

	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There are no tracks playing", e.getChannel());
			return;
		}
		
		if(isDJ(e)) {
			if(getListeners(e) == 1) {
				ms.getGuildAudioPlayer(e.getGuild()).scheduler.stopAndClear();
				UsrMsgUtil.sendVEMessage("Stopped playing and cleared the queue", e.getChannel());
				return;
			}
			e.reply("You need to have the **DJ** role in order to do that!");
			return;
		}
		ms.getGuildAudioPlayer(e.getGuild()).scheduler.stopAndClear();
		UsrMsgUtil.sendVEMessage("Stopped playing and cleared the queue", e.getChannel());
	}
}
