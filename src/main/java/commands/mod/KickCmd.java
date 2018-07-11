package commands.mod;

import java.util.LinkedList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import utility.core.UsrMsgUtil;

public class KickCmd extends Command {

	public KickCmd() {
		this.name = "kick";
        this.category = Bumblebot.Mod;
        this.arguments = "[@user...] <reason> {} @BumbleCore he took my nudes";
        this.help = "Kicks all mentioned users";
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
	}
	
	@Override
	protected void execute(CommandEvent e) {
		if(e.getMessage().getMentionedUsers().isEmpty()) {
			e.reply("You need to mention a user!");
		}else if(e.getMessage().getMentionedUsers().get(0).getId().equals(e.getSelfUser().getId())) {
			e.reply("You want me to kick myself? ;-;");
		}else if(e.getMessage().getMentionedUsers().size() > 0 && e.getMessage().getMentionedUsers().size() < 5) {

			LinkedList<User> users = new LinkedList<>();
	        StringBuilder builder = new StringBuilder();
	        e.getMessage().getMentionedUsers().stream().forEach((u) -> {
	        	Member m = e.getGuild().getMember(u);
	            if(m==null){
	                users.add(u);
	            }else if(!e.getMember().canInteract(m)){
	                builder.append("\n").append("You do not have permission to kick ").append(u.getAsMention());
	            }else if (!e.getSelfMember().canInteract(m)){
	                builder.append("\n").append("I do not have permission to kick ").append(u.getAsMention());
	            }else{
	                users.add(u);
	            }
	        });
	        
	        if(users.isEmpty()) {
        		UsrMsgUtil.sendEMessage(builder.toString(), e.getChannel());
	        }else{
	        	for(int i=0; i<users.size(); i++){
	        		User u = users.get(i);
	                boolean last = i+1==users.size();
	                String reason = e.getAuthor().getName()+"#"+e.getAuthor().getDiscriminator()+" kicked because... "+e.getArgs().replaceAll("<@!?\\d+>", "");
	                
	                if(reason.length()>512) {
	                    reason = reason.substring(0,512);
	                }
	                e.getGuild().getController().kick(u.getId(), reason).queue((v) -> {
	                	builder.append("\n").append("Sayonara, BITCH! ").append(u.getAsMention() + " just got kicked!");
	                	if(last) {
	                		UsrMsgUtil.sendVEMessage(builder.toString(), e.getChannel());
	                	}
	                }, (t) -> {
	                	builder.append("\n").append("I failed to kick ").append(u.getAsMention() + " ;-;");
	                	if(last) {
	                		UsrMsgUtil.sendVEMessage(builder.toString(), e.getChannel());
	                	}
	                });
	            }
	        }
		}
		
	}
}
