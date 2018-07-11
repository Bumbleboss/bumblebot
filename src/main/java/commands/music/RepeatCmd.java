package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class RepeatCmd extends Music {

	public RepeatCmd() {
		this.name = MUSIC_PREFIX + "repeat";
		this.help = "Repeats the current track";
		this.aliases = new String[] {MUSIC_PREFIX+"loop", MUSIC_PREFIX+"rpt"};
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There's no track playing to repeat", e.getChannel());
			return;
		}
		
		if(!isDJ(e)) {
			if(getListeners(e) == 1) {
				ms.repeatTrack(e.getTextChannel());
				return;
			}
			e.reply("You need to have the **"+DJ_ROLE+"** role in order to do that!");
			return;
		}
		ms.repeatTrack(e.getTextChannel());
	}
}
