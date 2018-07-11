package utility.audio;

import java.util.HashSet;
import java.util.Set;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import main.Bumblebot;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import utility.audio.queue.FairQueue;
import utility.audio.queue.QueuedTrack;

public class TrackScheduler extends AudioEventAdapter implements AudioSendHandler {
	
	private final AudioPlayer player;
	private final long guildId;
	private final FairQueue<QueuedTrack> queue;
	private final Set<String> votes;
	private AudioFrame lastFrame;
	private User requester;
	private boolean repeat = false;
	public TrackScheduler(AudioPlayer player, Guild guild) {
	    this.player = player;
	    this.guildId = guild.getIdLong();
	    queue = new FairQueue<QueuedTrack>();
	    votes = new HashSet<>();
	}
	
	public User getRequester() {
		return requester;
	}
	
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	public boolean isRepeating() {
		return repeat;
	}
	
	public int addTrack(AudioTrack track, User usr) {
		if(player.getPlayingTrack() ==  null) {
			requester = usr;
			player.playTrack(track);
			return -1;
		}else{
			return queue.add(new QueuedTrack(track, usr));
		}
	}
	
	public FairQueue<QueuedTrack> getQueue() {
		return queue;
	}
	
	public void stopAndClear() {
		queue.clear();
		player.stopTrack();
	}
	
	public boolean isMusicPlaying() {
		return Bumblebot.jda.getGuildById(guildId).getSelfMember().getVoiceState().inVoiceChannel() && player.getPlayingTrack() != null;
	}
	
	public Set<String> getVotes() {
		return votes;
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		votes.clear();
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if(endReason == AudioTrackEndReason.FINISHED && isRepeating()) {
			player.playTrack(track.makeClone());
			return;
		}
		
		if(endReason == AudioTrackEndReason.FINISHED || endReason == AudioTrackEndReason.STOPPED || endReason == AudioTrackEndReason.LOAD_FAILED) {
            if(queue.isEmpty()) {
            	if(isRepeating()) {
            		setRepeat(false);
            	}
            	if(player.isPaused()) {
            		player.setPaused(false);
            	}
            	votes.clear();
            	new Thread(new Runnable() {
        			public void run() {
        				Bumblebot.jda.getGuildById(guildId).getAudioManager().closeAudioConnection();
        			}
        		}).start();
            	return;
            }
            
            QueuedTrack qt = queue.pull();
            requester = qt.getOwner();
            player.playTrack(qt.getTrack());
		}
	}
	
	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame != null;
	}

    @Override
    public byte[] provide20MsAudio() {
    	return lastFrame.getData();
    }

    @Override
    public boolean isOpus() {
    	return true;
    }
}
