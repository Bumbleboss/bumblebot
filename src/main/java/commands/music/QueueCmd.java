package commands.music;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;

import net.dv8tion.jda.core.exceptions.PermissionException;
import utility.ConfigUtil;
import utility.audio.GuildMusicManager;
import utility.audio.MusicManager;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;

public class QueueCmd extends Music {
	
	private final Paginator.Builder pbuilder;
	public QueueCmd(EventWaiter waiter) {
		this.name = MUSIC_PREFIX+"queue";
		this.aliases = new String[] {MUSIC_PREFIX+"q"};
		this.help = "Checks the current queue";
		pbuilder = new Paginator.Builder()
				.setColumns(1)
                .setItemsPerPage(10)
                .showPageNumbers(true)
                .waitOnSinglePage(false)
                .useNumberedItems(false)
                .setFinalAction(m -> { try {
                        m.clearReactions().queue();
                    } catch(PermissionException ex) {
                        m.delete().queue();
                    }
                })
                .setEventWaiter(waiter)
                .setTimeout(1, TimeUnit.MINUTES);
	}

	@Override
	public void doCommand(CommandEvent e) {
		int page = 1;
		try {
			page = Integer.parseInt(e.getArgs());
		}catch(NumberFormatException ex) {}
		MusicManager ms = getMusicManager();
		GuildMusicManager musicManager = ms.getGuildAudioPlayer(e.getGuild());
	    List<QueuedTrack> queue = musicManager.scheduler.getQueue().getList();
	    
	    if(queue.isEmpty()) {
			if(ms.isPlaying(e.getGuild())) {
	    		UsrMsgUtil.sendEMessage("There are no tracks playing", e.getChannel());
	    		return;
	    	}
			ms.nowPlayingTrack(e.getTextChannel());
	    	return;
	    }
	    	    
	    String[] songs = new String[queue.size()];
        long total = 0;
	    for(int i = 0; i < queue.size(); i++) {
	    	total += queue.get(i).getTrack().getDuration();
	    	songs[i] = "**"+(i+1) + ".** "+ queue.get(i).toString(true, true, 50);
	    }

	    pbuilder.setText("Duration is... **"+ms.formatTime(total)+"**")
	    .setItems(songs)
	    .setUsers(e.getAuthor())
	    .setColor(Color.decode(ConfigUtil.getHex()))
	    .build().paginate(e.getChannel(), page);
	}
}
