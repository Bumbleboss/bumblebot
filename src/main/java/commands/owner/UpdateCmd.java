package commands.owner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class UpdateCmd extends Command {

	public UpdateCmd() {
		this.name = "update";
		this.help = "update the bot files";
		this.ownerCommand = true;
		this.category = Bumblebot.Owner;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		UsrMsgUtil.sendVEMessage("Updating bot!", e.getChannel());
		Thread th = new Thread(() -> {
			try {
				UsrMsgUtil.sendVEMessage("Update log:\n"+OtherUtil.postToHaste(processLog(Runtime.getRuntime().exec("./update"))), e.getChannel());
			} catch (IOException ex) {
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		});
		th.start();
	}

	private String processLog(Process process) {
		StringBuilder sb = new StringBuilder();
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		try {
			while ((line = input.readLine()) != null ) {
				sb.append(line).append("\n");
			}
			process.getInputStream().close();
		} catch (Exception ex) {
			if(ex instanceof IOException) {
				sb.append("IO EXCEPTION:\n").append(ex.getMessage()).append("\n");
			}else{
				sb.append("EXCEPTION:\n").append(ex.getMessage()).append("\n");
			}
		}
		return sb.toString();
	}
}
