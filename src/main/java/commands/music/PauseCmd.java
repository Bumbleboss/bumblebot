package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class PauseCmd extends Music {

	public PauseCmd() {
		this.name = MUSIC_PREFIX+"pause";
		this.help = "Pause a track";
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There are no tracks to pause", e.getChannel());
			return;
		}
		
		if(isDJ(e)) {
			if(getListeners(e) == 1) {
				ms.pauseTrack(e.getTextChannel());
				return;
			}
			e.reply("You need to have the **"+DJ_ROLE+"** role in order to do that!");
			return;
		}
		ms.pauseTrack(e.getTextChannel());
	}
}
