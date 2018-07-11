package commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.myServer.roles.RolesManager;
import main.Bumblebot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import utility.audio.MusicManager;
import utility.core.UsrMsgUtil;

public abstract class Music extends Command{

	//DON'T EVEN THINK OF EDITING IT
	public static MusicManager ms = new MusicManager();
	public String MUSIC_PREFIX = "m";
	public String DJ_ROLE = "DJ";
	
	public Music() {
		this.category = Bumblebot.Music;
		this.guildOnly = true;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		GuildVoiceState usrStat = e.getMember().getVoiceState();
		GuildVoiceState botStat = e.getGuild().getSelfMember().getVoiceState();
		
		if(!usrStat.inVoiceChannel()) {
			UsrMsgUtil.sendEMessage("You need to be in a voice channel!", e.getChannel());
			return;
		}
		
		if(usrStat.inVoiceChannel() && botStat.inVoiceChannel() && !(usrStat.getChannel() == botStat.getChannel())) {
			UsrMsgUtil.sendEMessage("You need to be in the same voice channel to use that!", e.getChannel());
			return;
		}
		
		doCommand(e);
	}
	
	public MusicManager getMusicManager() {
	    return ms;
	}
	
    public boolean isDJ(CommandEvent e) {
        if(e.isOwner())
            return true;
        if(e.getMember().hasPermission(Permission.MANAGE_SERVER))
            return true;
        if(getMusicManager().getGuildAudioPlayer(e.getGuild()).scheduler.getRequester().getId().equals(e.getAuthor().getId())) 
        	return true;
        return RolesManager.hasRole(e.getMember(), DJ_ROLE);
    }
    
    public int getListeners(CommandEvent e) {
        return (int) e.getSelfMember().getVoiceState().getChannel().getMembers().stream().filter(m -> !m.getUser().isBot() && !m.getVoiceState().isDeafened()).count();
    }
    
	public abstract void doCommand(CommandEvent event);
}
