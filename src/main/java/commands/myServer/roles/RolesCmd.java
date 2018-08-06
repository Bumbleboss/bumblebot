package commands.myServer.roles;

import java.awt.Color;
import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;

import commands.myServer.Server;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

public class RolesCmd extends Server{
	
	public RolesCmd() {
		this.name = "roles";
		this.help = "Check the roles that can be assigned on the server.";
	}

	@Override
	public void doCommand(CommandEvent e) {
		StringBuilder eb = new StringBuilder();
		StringBuilder eb1 = new StringBuilder();
		StringBuilder eb2 = new StringBuilder();
		StringBuilder eb3 = new StringBuilder();

		List<Object> list = RolesManager.getRoles().toList();
		for(int i = 0; i < list.size(); i++) {
			if(RolesManager.getRoleType(i).equals("perks")) {
				eb.append("***").append(RolesManager.getRoleName(i)).append("*** - *").append(RolesManager.getRoleDes(i)).append("*\n");
			}
			if(RolesManager.getRoleType(i).equals("gender")) {
				eb1.append("***").append(RolesManager.getRoleName(i)).append("*** - *").append(RolesManager.getRoleDes(i)).append("*\n");
			}
			if(RolesManager.getRoleType(i).equals("special")) {
				eb2.append("***").append(RolesManager.getRoleName(i)).append("*** - *").append(RolesManager.getRoleDes(i)).append("*\n");
			}
			if(RolesManager.getRoleType(i).equals("coding")) {
				eb3.append("**").append(RolesManager.getRoleName(i)).append("** | ");
			}
		}
		
		e.reply(new EmbedBuilder().addField("Usage", "To add a role, type **" + ConfigUtil.getPrefix()+ new AddRolesCmd().getName() + " <role_name>**", false)
				.addField("Genders", eb1.toString(), false).addField("Perks", eb.toString(), false).addField("Special", eb2.toString(), false)
				.addField("Coding roles", eb3.toString(), false).addField("Custom", "Only given to whom is trusted/active on the server.", false)
				.setColor(Color.decode(ConfigUtil.getHex())).build());
	}
}
