package commands.music;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.ConfigUtil;
import utility.audio.MusicManager;
import utility.core.FileManager;
import utility.core.UsrMsgUtil;

public class PlaylistCmd extends Music {
	
	public PlaylistCmd() {
		this.name = MUSIC_PREFIX+"playlist";
		this.aliases = new String[] {MUSIC_PREFIX+"pl"};
		this.help = "Play any of the playlists that are present in the bot files!";
		this.arguments = "<playlist_name | list> {} dance";
	}

	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		FileManager fl = new FileManager("./assists/playlists/");
		StringBuilder sb = new StringBuilder();
		List<String> playlists = fl.listFiles("./assists/playlists/", "txt");
		
		if(ms.getGuildAudioPlayer(e.getGuild()).player.isPaused()) {
			ms.pauseTrack(e.getTextChannel());
			return;
		}else if(e.getArgs().isEmpty()) {
			UsrMsgUtil.sendEMessage("Usage: **" + ConfigUtil.getPrefix() + this.name + " " + this.arguments.split("\\{}")[0]+"**", e.getChannel());
			return;
		}
		
		for(int i = 0; i < playlists.size(); i++) {
			if(e.getArgs().equalsIgnoreCase("list")) {
				sb.append("***"+playlists.get(i).replace(".txt", "")+"*** | ");
			}else if(e.getArgs().equalsIgnoreCase(playlists.get(i).replace(".txt", "").replace("_", "").replace(" ", ""))) {
				if(!e.getSelfMember().getVoiceState().inVoiceChannel()) {
					ms.connectToVc(e.getGuild().getAudioManager(), e.getMember());
				}
				ms.loadCustom(e.getTextChannel(), FileManager.readFiles(playlists.get(i)), e.getAuthor());
				return;
			}
		}
		
		if(sb.toString().length() > 0) {
			UsrMsgUtil.sendVEMessage("**All playlists:**\n"+sb.toString(), e.getChannel());
			return;
		}
		
		if(playlists.size() == 0) {
			UsrMsgUtil.sendVEMessage("There are no playlists within the bot files!", e.getChannel());
			return;
		}
		
		UsrMsgUtil.sendVEMessage("Playlist not found!", e.getChannel());
	}
}
