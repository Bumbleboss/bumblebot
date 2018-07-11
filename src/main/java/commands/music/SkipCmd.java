package commands.music;

import java.util.Set;

import com.jagrosh.jdautilities.command.CommandEvent;
import utility.audio.MusicManager;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;

public class SkipCmd extends Music {

	public SkipCmd() {
		this.name = MUSIC_PREFIX+"skip";
		this.aliases = new String[] {MUSIC_PREFIX+"s"};
		this.help = "Skip current playing track";
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There's no track playing to skip", e.getChannel());
			return;
		}
				
		if(!isDJ(e)) {
			if(getListeners(e) == 1) {
				ms.skipTrack(e.getTextChannel(), e.getAuthor(), true, null);
				return;
			}

			String tr = new QueuedTrack(ms.getGuildAudioPlayer(e.getGuild()).player.getPlayingTrack(), null).toString(false, false, 0);
			String msg;
			
			Set<String> votes = ms.getGuildAudioPlayer(e.getGuild()).scheduler.getVotes();
			if(votes.contains(e.getAuthor().getId())) {
				msg = "You already voted to skip " + tr;
			}else{
				msg = "You voted to skip " + tr;
				votes.add(e.getAuthor().getId());
			}
			
			int skps = (int)e.getSelfMember().getVoiceState().getChannel().getMembers().stream()
                    .filter(m -> votes.contains(m.getUser().getId())).count();
            int rqrd = (int)Math.ceil(getListeners(e) * .55);
			msg += "\n**"+skps+"/"+rqrd+"** needed";
			
			if(skps>=rqrd) {
				ms.skipTrack(e.getTextChannel(), e.getAuthor(), true, "");
			}else{
				ms.skipTrack(e.getTextChannel(), e.getAuthor(), false, msg);
			}
			return;
		}	
		ms.skipTrack(e.getTextChannel(), e.getAuthor(), true, null);
	}
}
