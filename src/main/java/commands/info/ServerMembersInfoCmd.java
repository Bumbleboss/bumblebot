package commands.info;


import java.awt.Color;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class ServerMembersInfoCmd extends Command {

	public ServerMembersInfoCmd() {
		this.name = "servermembers";
		this.help = "A detailed information about the members of the server.";
		this.aliases = new String[] {"srvrmem", "sm"};
		this.category = Bumblebot.Info;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		Guild guild = e.getGuild();
		EmbedBuilder eb = new EmbedBuilder();
        int[] all = UsrMsgUtil.getMembers(guild);

		StringBuilder sb = new StringBuilder();
		sb.append("<:Online:421600843863687168>Online: **" + all[0] + "**").append("\n");
		sb.append("<:Idle:421601342797381642>Away: **" + all[2] + "**").append("\n");
		sb.append("<:Do_Not_Disturb:421601568299941889>Do not disturb: **" + all[3] + "**").append("\n");
		sb.append("<:Offline:421601574255591454>Offline: **" + all[1] + "**");
		eb.addField("User Status", sb.toString(), true);
		
		StringBuilder sb1 = new StringBuilder();
		sb1.append("Users:\n");
		for(int i = 0; i < guild.getMembers().size(); i++) {
			if(!guild.getMembers().get(i).getUser().isBot()) {
				String memname = guild.getMembers().get(i).getEffectiveName();
				String memid = guild.getMembers().get(i).getUser().getId();
				sb1.append("  "+memname+" (" + memid + ")" + "\n");
			}
		}
		sb1.append("\n\nBots:\n");
		for(int i = 0; i < guild.getMembers().size(); i++) {
			if(guild.getMembers().get(i).getUser().isBot()) {
				String memname = guild.getMembers().get(i).getEffectiveName();
				String memid = guild.getMembers().get(i).getUser().getId();
				sb1.append("  "+memname+" (" + memid + ")" + "\n");
			}
		}

		eb.addField("Members","[List of members]("+OtherUtil.postToHaste(sb1.toString())+".txt)", false);
		eb.setColor(Color.decode(ConfigUtil.getHex()));
		e.reply(eb.build());
	}
}
