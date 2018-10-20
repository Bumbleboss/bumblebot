package commands.myServer.roles;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.myServer.Server;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import utility.core.UsrMsgUtil;

public class AddRolesCmd extends Server {

	public AddRolesCmd() {
		this.name = "addrole";
		this.help = "Assigning self-roles instead of an admin doing the work.";
		this.arguments = "<roleName> {} booblings";
	}

	@Override
	public void doCommand(CommandEvent e) {
		Guild gld = e.getGuild();
		String role = e.getArgs();
		String roleName = RolesManager.getAdjustedRole(role);

		if(roleName == null) {
			roleName = role;
		}
		
		if(RolesManager.hasRole(e.getMember(), roleName)) {
			UsrMsgUtil.sendEMessage("You already have this role!", e.getChannel());
		}else if(!gld.getRolesByName(roleName, true).isEmpty()) { 
			if(RolesManager.matchRole(roleName, gld)) {
				if(RolesManager.assingableRole(roleName, gld)) {
					gld.getController().addRolesToMember(e.getMember(), gld.getRolesByName(roleName, true)).queue();
					e.reply(new EmbedBuilder().setColor(e.getGuild().getRolesByName(roleName, true).get(0).getColor())
							.setAuthor(e.getAuthor().getName(), null, e.getAuthor().getAvatarUrl())
							.setDescription("The role **" + roleName + "** has been added. ").build());
				}else{
					UsrMsgUtil.sendEMessage("You need BumbleCore's permission to add this role.", e.getChannel());
				}
			}else{
				UsrMsgUtil.sendEMessage("Sorry, the requested role is not self-assignable.", e.getChannel());
			}
		}else{
			UsrMsgUtil.sendEMessage("There is no such role called **"+roleName+"**", e.getChannel());
		}
	}
}
