package commands.misc;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class ServerInviteCmd extends Command{

	public ServerInviteCmd() {
		this.name = "invite";
		this.help = "Invitation link to BumbleCore's server.";
		this.category = Bumblebot.Misc;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		if(e.getGuild().getId().equals(ConfigUtil.getServerId())) {
			UsrMsgUtil.sendVEMessage("Inviting people here is always fun!\nInvite link: https://discord.gg/7PCdKYN", e.getChannel());
		}else{
			UsrMsgUtil.sendVEMessage("Hope you enjoy your stay there!\nInvite link: https://discord.gg/7PCdKYN", e.getChannel());
		}
	}
}
