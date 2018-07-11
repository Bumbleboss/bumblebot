package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class NowPlayingCmd extends Music {
	
	public NowPlayingCmd() {
		this.name = MUSIC_PREFIX+"nowplaying";
		this.aliases = new String[] {MUSIC_PREFIX+"np"};
		this.help = "Check the current playing track";
	}

	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There is no track playing right now", e.getChannel());
			return;
		}
		ms.nowPlayingTrack(e.getTextChannel());
	}
}
