package utility.audio.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.User;

public class QueuedTrack implements Queueable {
	
	private final AudioTrack track;
	private final User owner;
	
	public QueuedTrack(AudioTrack track, User owner) {
		this.track = track;
		this.owner = owner;
	}
	
	@Override
    public long getIdentifier() {
        return owner.getIdLong();
    }
	
	public User getOwner() {
		return owner;
	}
	
	public AudioTrack getTrack() {
		return track;
	}

	public String toString(boolean ownr, boolean sub, int substring) {
		String title = null;
		String orgTit = track.getInfo().title;
		if(orgTit.contains("[") && orgTit.contains("]")) {
			title = orgTit.replace("[", "").replace("]", "");
		}else{
			title = orgTit;
		}
		
		if(sub) {
			if(title.length() > substring) {
	    		return "["+title.substring(0, substring)+"...]("+track.getInfo().uri + ")" + (ownr?" | "+owner.getName():"");
	    	}else{
	    		return "["+title+"]("+track.getInfo().uri + ")"+(ownr?" | "+owner.getName():"");
	    	}
		}else{
    		return "["+title+"]("+track.getInfo().uri + ")"+(ownr?" | "+owner.getName():"");
		}
	}
}
