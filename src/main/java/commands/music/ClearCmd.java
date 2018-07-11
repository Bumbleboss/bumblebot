package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class ClearCmd extends Music {

	public ClearCmd() {
		this.name = MUSIC_PREFIX+"clear";
		this.help = "Clears the current queue";
		this.aliases = new String[] {MUSIC_PREFIX+"clr"};
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There are no tracks in the queue", e.getChannel());
			return;
		}
		
		if(!isDJ(e)) {
			if(getListeners(e) == 1) {
				ms.clearQueue(e.getTextChannel());
				return;
			}
			e.reply("You need to have the **"+DJ_ROLE+"** role in order to do that!");
			return;
		}
		ms.clearQueue(e.getTextChannel());
	}
}
