package commands.mod;

import java.util.List;
import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import utility.core.UsrMsgUtil;
import net.dv8tion.jda.core.Permission;

public class MuteCmd extends Command {
    
    public MuteCmd() {
        this.name = "mute";
        this.arguments = "@user [@user...]";
        this.help = "Adds a muted role to the mentioned users.";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent e) {
        if(e.getMessage().getMentionedUsers().isEmpty()){
        	UsrMsgUtil.sendEMessage("You need to mention a user!", e.getChannel());
        	return;
        }
        if(e.getMessage().getMentionedUsers().size()>5){
        	UsrMsgUtil.sendEMessage("You can only mute five users at once.", e.getChannel());
            return;
        }
        Role muteRole = e.getGuild().getRoles().stream().filter(r -> r.getName().equalsIgnoreCase("muted")).findFirst().orElse(null);
        if(muteRole == null){
        	UsrMsgUtil.sendEMessage("Role 'Muted' does not exist", e.getChannel());
            return;
        }
        if(!e.getMember().canInteract(muteRole)){
        	UsrMsgUtil.sendEMessage("You don't have the permission to assign the 'Muted' role!", e.getChannel());
            return;
        }
        if(!e.getSelfMember().canInteract(muteRole)){
        	UsrMsgUtil.sendEMessage("I don't have the permission to assign the 'Muted' role!", e.getChannel());
            return;
        }
        
        StringBuilder builder = new StringBuilder();
        List<Member> members = e.getMessage().getMentionedUsers().stream()
                .map(u -> e.getGuild().getMember(u))
                .filter((Member m) -> {
                    if(m==null){
                        builder.append("\n").append("User is not in the server!");
                        return false;
                    }
                    if(m.equals(e.getSelfMember())){
                        builder.append("\n").append("You want me to mute myself? ;-;");
                        return false;
                    }
                    if(m.getRoles().contains(muteRole)){
                        builder.append("\n").append(m.getUser().getAsMention()).append(" is already muted!");
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
        
        String reason = e.getAuthor().getName()+"#"+e.getAuthor().getDiscriminator()+" muted because... "+e.getArgs().replaceAll("<@!?\\d+>", "");
        
        if(reason.length()>512) {
            reason = reason.substring(0,512);
        }
        
        if(members.isEmpty()){
        	UsrMsgUtil.sendEMessage(builder.toString(), e.getChannel());
        }else for(int i=0; i<members.size(); i++){
            Member m = members.get(i);
            boolean last = i+1==members.size();
            e.getGuild().getController().addRolesToMember(m, muteRole).reason(reason).queue(v -> {
                    builder.append("\n").append("Rip, ").append(m.getUser().getAsMention()).append(" just got muted.");
                    if(last)
                    	UsrMsgUtil.sendVEMessage(builder.toString(), e.getChannel());
            }, t -> {
                    builder.append("\n").append(e.getClient().getError()).append(" I failed to mute ").append(m.getUser());
                    if(last)
                    	UsrMsgUtil.sendEMessage(builder.toString(), e.getChannel());
            });
        }
    }
}
