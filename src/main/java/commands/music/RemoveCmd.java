package commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import utility.audio.MusicManager;
import utility.audio.queue.FairQueue;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;

public class RemoveCmd extends Music{
	
	public RemoveCmd() {
		this.name = MUSIC_PREFIX+"remove";
		this.help = "Removes a track from the queue";
		this.arguments = "<position|all";
		this.aliases = new String[] {MUSIC_PREFIX+"rem"};
	}

	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		FairQueue<QueuedTrack> queue = ms.getGuildAudioPlayer(e.getGuild()).scheduler.getQueue();

		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There are no tracks playing", e.getChannel());
			return;
		}		
		
		if(e.getArgs().equalsIgnoreCase("all")) {
			int num = queue.removeAll(e.getAuthor().getIdLong());
			if(num == 0) {
				UsrMsgUtil.sendEMessage("You don't have any tracks in the queue", e.getChannel());
			}else{
				UsrMsgUtil.sendVEMessage("Removed your **"+num+"** entries from the queue", e.getChannel());
			}
			return;
		}
		
		int pos;
		try {
			pos = Integer.parseInt(e.getArgs());
		}catch (NumberFormatException ex) {
			pos = 0;
		}
		
		if(pos < 1 || pos > queue.size()) {
			UsrMsgUtil.sendEMessage("Postion must be between **1** and **"+queue.size()+"**", e.getChannel());
			return;
		}
		
		UsrMsgUtil.sendVEMessage("Removed "+ queue.get(pos-1).toString(false, false, 0) + " from the queue", e.getChannel());
		queue.remove(pos-1);
	}
}
