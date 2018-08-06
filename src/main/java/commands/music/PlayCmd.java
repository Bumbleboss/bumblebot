package commands.music;


import com.jagrosh.jdautilities.command.CommandEvent;

import utility.ConfigUtil;
import utility.OtherUtil;
import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;


public class PlayCmd extends Music {
	
	public PlayCmd() {
		this.name = MUSIC_PREFIX+"play";
		this.aliases = new String[] {MUSIC_PREFIX+"p"};
		this.arguments = "<URL | Query | File> {} Kozah - Dream State";
		this.help = "Play a track right away!";
	}

	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		String url;
		if(OtherUtil.isValidURL(e.getArgs())) {
			url = e.getArgs();
		}else if(!e.getMessage().getAttachments().isEmpty()) {
			url = e.getMessage().getAttachments().get(0).getUrl();
		}else{
			url = "ytsearch: " + e.getArgs();
		}
		
		if(ms.getGuildAudioPlayer(e.getGuild()).player.isPaused()) {
			ms.pauseTrack(e.getTextChannel());
		}else if(e.getArgs().isEmpty()) {
			UsrMsgUtil.sendEMessage("Usage: **" + ConfigUtil.getPrefix() + this.name + " " + this.arguments.split("\\{}")[0]+"**", e.getChannel());
		}else{
			if(!e.getSelfMember().getVoiceState().inVoiceChannel()) {
				ms.connectToVc(e.getGuild().getAudioManager(), e.getMember());
			}
			ms.loadTracks(e.getTextChannel(), url, e.getAuthor(), null, null);
		}
	}
	
}
