package commands.music;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public class PositionCmd extends Music {

	public PositionCmd() {
		this.name = MUSIC_PREFIX+"setpos";
		this.help = "Sets the position of the track";
		this.arguments = "0h 0m 0s {} 1m 23s";
		this.aliases = new String[] {MUSIC_PREFIX+"sp"};
	}
	
	@Override
	public void doCommand(CommandEvent e) {
		MusicManager ms = getMusicManager();
		if(ms.isPlaying(e.getGuild())) {
			UsrMsgUtil.sendEMessage("There is no track playing right now", e.getChannel());
			return;
		}
		
		String pos = e.getArgs();
		String regex = 
				  "([0-9]|[1-5][0-9]|60)h([0-9]|[1-5][0-9]|60)m([0-9]|[1-5][0-9]|60)s|"
				+ "([0-9]|[1-5][0-9]|60)h([0-9]|[1-5][0-9]|60)m|"
				+ "([0-9]|[1-5][0-9]|60)m([0-9]|[1-5][0-9]|60)s|"
				+ "([0-9]|[1-5][0-9]|60)h|"
				+ "([0-9]|[1-5][0-9]|60)m|"
				+ "([0-9]|[1-5][0-9]|60)s";
		
		if(pos.replace(" ", "").matches(regex)) {
			long time = getTime(pos);
			if(time > ms.getGuildAudioPlayer(e.getGuild()).player.getPlayingTrack().getDuration()) {
				UsrMsgUtil.sendEMessage("Cannot change position that is above the track duration!", e.getChannel());
			}else{
				ms.getGuildAudioPlayer(e.getGuild()).player.getPlayingTrack().setPosition(time);
				UsrMsgUtil.sendVEMessage("Position has been changed to **"+ms.formatTime(time)+"**", e.getChannel());
			}
		}else{
			UsrMsgUtil.sendEMessage("Invalid format, correct format should be **0h 0m 0s**", e.getChannel());
		}
	}
	
	private long getTime(String time) {
		String[] h = time.split("[h]");
		String[] m = time.split("[m]");
		String[] s = time.split("[s]");
		
		long hours = 0;
		try {
			hours = Integer.parseInt(h[0]);
		}catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {}
		
		long minutes = 0;
		try {
			if(m[0].matches("([0-9]|[1-5][0-9]|60)h ([0-9]|[1-5][0-9]|60)")) {
				minutes = Integer.parseInt(m[0].replace(Integer.toString((int) hours)+"h ", ""));
			}else{
				minutes = Integer.parseInt(m[0]);
			}
		}catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {}
		
		long seconds = 0;
		try {
			if(s[0].matches("([0-9]|[1-5][0-9]|60)h ([0-9]|[1-5][0-9]|60)m ([0-9]|[1-5][0-9]|60)")) {
				seconds = Integer.parseInt(s[0].replace(Integer.toString((int) hours)+"h " + Integer.toString((int) minutes)+"m ", ""));
			}else if(s[0].matches("([0-9]|[1-5][0-9]|60)m ([0-9]|[1-5][0-9]|60)")) {
				seconds = Integer.parseInt(s[0].replace(Integer.toString((int) minutes)+"m ", ""));
			}else{
				seconds = Integer.parseInt(s[0]);
			}
		}catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {}
		
		return getMillisecond(hours, minutes, seconds);
	}
	
	private long getMillisecond(long hr, long min, long sec) {
		long hrs = TimeUnit.MILLISECONDS.convert(hr, TimeUnit.HOURS);
		long mins = TimeUnit.MILLISECONDS.convert(min, TimeUnit.MINUTES);
		long secs = TimeUnit.MILLISECONDS.convert(sec, TimeUnit.SECONDS);
		return (hrs+mins+secs);
	}
}
