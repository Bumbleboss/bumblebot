package commands.music;

import java.awt.Color;

import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.audio.GuildMusicManager;
import utility.audio.MusicManager;
import utility.audio.queue.FairQueue;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;

public class SkipToCmd extends Music{

	public SkipToCmd() {
		this.name = MUSIC_PREFIX + "skipto";
		this.help = "Skip to a track from the queue";
		this.arguments = "<position> {} 1";
		this.aliases = new String[] {MUSIC_PREFIX+"st"};
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		
		if(!isDJ(e)) {
			if(getListeners(e) == 1) {}
			e.reply("You need to have the **"+DJ_ROLE+"** role in order to do that!");
			return;
		}
		
		int num = 0;
		try {
			num = Integer.parseInt(e.getArgs());
		}catch (NumberFormatException ex) {
			UsrMsgUtil.sendEMessage("**"+e.getArgs()+"** is not a valid position", e.getChannel());
			return;
		}
		
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There's no track playing to skip to", e.getChannel());
			return;
		}
		
		GuildMusicManager gm = ms.getGuildAudioPlayer(e.getGuild());
		FairQueue<QueuedTrack> queue = gm.scheduler.getQueue();
		
		if(num < 1 || num > queue.size()) {
			UsrMsgUtil.sendEMessage("Position must be between **1** and **" + queue.size()+"**", e.getChannel());
			return;
		}
		
		gm.scheduler.getQueue().skip(num-1);
		e.reply(new EmbedBuilder().setAuthor(e.getAuthor().getName(), null, e.getAuthor().getAvatarUrl())
				.setColor(Color.decode(ConfigUtil.getHex()))
				.setDescription("Skipped to " + queue.get(0).toString(false, false, 0))
				.build());
		gm.scheduler.getPlayer().stopTrack();
	}
}
