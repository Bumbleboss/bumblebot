package commands.myServer.roles;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;

import commands.myServer.Server;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import utility.core.UsrMsgUtil;

public class RemoveRolesCmd extends Server {
	
	public RemoveRolesCmd() {
		this.name = "remrole";
		this.help = "Removing self-roles instead of an admin doing the work.";
		this.arguments = "<roleName> {} booblings";
	}

	@Override
	public void doCommand(CommandEvent e) {
		Guild gld = e.getGuild();
		String role = e.getArgs();
			
		String roleName = null;
		List<Object> list = RolesManager.getRoles().toList();
		for(int i = 0; i < list.size(); i++) {
			if(RolesManager.getRoleTypo(i).equalsIgnoreCase(role)) {
				roleName = RolesManager.getRoleName(i);
				break;
			}else if(RolesManager.getRoleName(i).equalsIgnoreCase(role)) {
				roleName = RolesManager.getRoleName(i);
				break;
			}
		}
			
		if(roleName == null) {
			roleName = role;
		}
			
		if(gld.getRolesByName(roleName, true).isEmpty()) {
			UsrMsgUtil.sendEMessage("This role does not exist", e.getChannel());
		}else if(!RolesManager.hasRole(e.getMember(), roleName)) {
			UsrMsgUtil.sendEMessage("You don't have that role added!", e.getChannel());
		}else{
			gld.getController().removeRolesFromMember(e.getMember(), gld.getRolesByName(roleName, true)).queue();
			e.reply(new EmbedBuilder().setColor(gld.getRolesByName(roleName, true).get(0).getColor())
					.setAuthor(e.getAuthor().getName(), null, e.getAuthor().getAvatarUrl())
					.setDescription("The role **" + roleName + "** has been removed.").build());
		}
	}
}
