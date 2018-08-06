package utility.audio;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import utility.ConfigUtil;
import utility.audio.queue.QueuedTrack;
import utility.core.UsrMsgUtil;
import utility.jdu.OrderMenu;

public class MusicManager {
	
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	private boolean syt = false;
	public MusicManager() {
		this.musicManagers = new HashMap<>();
	    this.playerManager = new DefaultAudioPlayerManager();
	    AudioSourceManagers.registerRemoteSources(playerManager);
	    AudioSourceManagers.registerLocalSource(playerManager);
	}	

	public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
	    long guildId = guild.getIdLong();
	    GuildMusicManager musicManager = musicManagers.get(guildId);
	    if(musicManager == null) {
	    	musicManager = new GuildMusicManager(playerManager, guild);
	    	musicManagers.put(guildId, musicManager);
	    }
	    guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
	    return musicManager;
	}
	
	public void loadCustom(final TextChannel channel, final List<String> tracks, final User usr) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		for (String track : tracks) {
			playerManager.loadItemOrdered(musicManager, track, new AudioLoadResultHandler() {
				@Override
				public void trackLoaded(AudioTrack track) {
					play(musicManager, track, usr);
				}

				@Override
				public void playlistLoaded(AudioPlaylist playlist) {
					playlist.getTracks().forEach((track) -> play(musicManager, track, usr));
				}

				@Override
				public void noMatches() {}

				@Override
				public void loadFailed(FriendlyException exception) {}
			});
		}
		UsrMsgUtil.sendVEMessage("Loaded **" +tracks.size() +(tracks.size()==1?"** link!":"** links!") + "\nStarting with " + tracks.get(0), channel);
	}
	
	public void loadTracks(final TextChannel channel, final String track, final User usr, OrderMenu.Builder bd, Message m) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if(bd != null) {
			syt = true;
		}
		
		playerManager.loadItemOrdered(musicManager, track, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				UsrMsgUtil.sendVEMessage("Added to queue " + new QueuedTrack(track, null).toString(false, false, 0), channel);
				play(musicManager, track, usr);
			}

			@SuppressWarnings("RedundantArrayCreation")
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				if(syt) {
					List<AudioTrack> trs = playlist.getTracks();
					Objects.requireNonNull(bd).setColor(Color.decode(ConfigUtil.getHex()))
					.setChoices(new String[0])
					.setDescription("Results")
					.setSelection((msg, i) -> {
						play(musicManager, trs.get(i-1), usr);
						UsrMsgUtil.sendVEMessage("Added to queue " + new QueuedTrack(trs.get(i-1), null).toString(false, false, 0), channel);
					})
					.setCancel((msg) -> UsrMsgUtil.sendEMessage("Selection has been canceled", channel))
					.setUsers(usr);
					for(int i = 0; i < 3 && i < trs.size(); i++) {
						AudioTrack tracks = trs.get(i);
						bd.addChoices(new QueuedTrack(tracks, null).toString(false, false, 0)+" - (" + formatTime(trs.get(i).getDuration()) + ")");
					}
					bd.build().display(m);
					syt = false;
					return;
				}
				
				if(playlist.isSearchResult()) {
					AudioTrack frT = playlist.getSelectedTrack();
					if(frT == null) {
						frT = playlist.getTracks().get(0);
					}
					play(musicManager, frT, usr);
					UsrMsgUtil.sendVEMessage("Added to queue " + new QueuedTrack(frT, null).toString(false, false, 0), channel);
					return;
				}
				
				List<AudioTrack> trs = playlist.getTracks();
				playlist.getTracks().forEach((track) -> play(musicManager, track, usr));
				AudioTrack firstTrack = playlist.getTracks().get(0);
				UsrMsgUtil.sendVEMessage("Added playlist **"+ playlist.getName()+"** with **"+ trs.size() +"** enteries!\nStarting with... "+ new QueuedTrack(firstTrack, null).toString(false, false, 0), channel);
			}

			@Override
			public void noMatches() {
				UsrMsgUtil.sendEMessage("Nothing found from... **"+track+"**", channel);
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				UsrMsgUtil.sendEMessage("Cound not play due to\n```"+exception.getMessage()+"```", channel);
			}
		});
	}
	
	public void skipTrack(TextChannel channel, User usr, boolean skip, String msg) {
		AudioPlayer plyr = getGuildAudioPlayer(channel.getGuild()).player;
		String content = "Skipped "+new QueuedTrack(plyr.getPlayingTrack(), null).toString(false, false, 0) +" from the queue!";
		
		if(skip) {plyr.stopTrack();}
		channel.sendMessage(new EmbedBuilder().setAuthor(usr.getName(), null, usr.getAvatarUrl())
				.setColor(Color.decode(ConfigUtil.getHex())).setDescription(skip?content:msg).build()).queue();
	}
	
	private void play(GuildMusicManager musicManager, AudioTrack track, User usr) {
		musicManager.scheduler.addTrack(track, usr);
	}
	
	public void clearQueue(TextChannel channel) {
	    getGuildAudioPlayer(channel.getGuild()).scheduler.getQueue().clear();
		UsrMsgUtil.sendVEMessage("Queue has been cleared...", channel);
	}
	
	public boolean isPlaying(Guild guild) {
		return getGuildAudioPlayer(guild).player.getPlayingTrack() == null;
	}
	
	public void pauseTrack(TextChannel channel) {
	    AudioPlayer plyr = getGuildAudioPlayer(channel.getGuild()).player;
	    if(plyr.isPaused()) {
	    	plyr.setPaused(false);
	    	UsrMsgUtil.sendVEMessage("Track has been resumed...", channel);
	    }else{
	    	plyr.setPaused(true);
	    	UsrMsgUtil.sendVEMessage("Track has been paused", channel);
	    }
	}
	
	public void repeatTrack(TextChannel channel) {
	    TrackScheduler shdr = getGuildAudioPlayer(channel.getGuild()).scheduler;
	    if(shdr.isRepeating()) {
	    	shdr.setRepeat(false);
	    	UsrMsgUtil.sendVEMessage("Repeat mode has been disabled", channel);
	    }else{
	    	shdr.setRepeat(true);
	    	UsrMsgUtil.sendVEMessage("Repeat mode has been enabled", channel);
	    }
	}
	
	public void nowPlayingTrack(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		AudioTrack trr = musicManager.player.getPlayingTrack();
	    double progress = (double)trr.getPosition()/trr.getDuration();
	    User usr = musicManager.scheduler.getRequester();
	    EmbedBuilder eb = new EmbedBuilder();
	    
	    eb.setAuthor("Now playing");
	    eb.setColor(Color.decode(ConfigUtil.getHex()));
	    
	    if(trr instanceof YoutubeAudioTrack) {
	    	eb.setThumbnail("https://img.youtube.com/vi/"+trr.getIdentifier()+"/mqdefault.jpg");
	    }
	    
	    if(formatTime(trr.getDuration()).equals("LIVE")) {
	    	eb.setDescription(new QueuedTrack(trr, usr).toString(false, true, 50) + (musicManager.scheduler.isRepeating() ? "\n*... And repeating*" : ""));
		    eb.setFooter("LIVE - Have been playing it for... " + formatTime(trr.getPosition()) + " | " + UsrMsgUtil.getUserSet(Bumblebot.jda, usr.getId()), null);
	    }else{
	    	eb.setDescription(new QueuedTrack(trr, usr).toString(false, true, 50) + (musicManager.scheduler.isRepeating() ? "\n*... And repeating*" : ""));
		    eb.setFooter(progressBar(progress) +"\n["+formatTime(trr.getPosition())+"/"+formatTime(trr.getDuration())+"] | " + UsrMsgUtil.getUserSet(Bumblebot.jda, usr.getId()), null);
	    }
	    channel.sendMessage(eb.build()).queue();
	}
	
	public void connectToVc(AudioManager audioManager, Member member) {
		if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
			VoiceChannel myChannel = member.getVoiceState().getChannel();
			audioManager.openAudioConnection(myChannel);	
		}
	}
	
	private static String progressBar(double percent) {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<12; i++)
            if(i == (int)(percent*12))
                str.append("⚫");
            else
                str.append("▬");
        return str.toString();
    }
	
	public String formatTime(long duration) {
        if(duration == Long.MAX_VALUE)
            return "LIVE";
        long seconds = Math.round(duration/1000.0);
        long hours = seconds/(60*60);
        seconds %= 60*60;
        long minutes = seconds/60;
        seconds %= 60;
        return (hours>0 ? hours+":" : "") + (minutes<10 ? "0"+minutes : minutes) + ":" + (seconds<10 ? "0"+seconds : seconds);
    }
}