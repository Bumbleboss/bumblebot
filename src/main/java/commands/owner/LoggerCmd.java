package commands.owner;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import utility.OtherUtil;
import utility.core.FileManager;
import utility.core.UsrMsgUtil;

public class LoggerCmd extends Command {
	
	public LoggerCmd() {
		this.name = "log";
		this.help = "All the arguements that is logger related";
		this.arguments = "[<date(0d 0m)>|latest|<loggerLevel; message>] {} warn; I'm about to shut my booty!!";
		this.ownerCommand = true;
		this.guildOnly = false;
		this.category = Bumblebot.Owner;
		this.aliases = new String[] {"logs", "logger"};
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent e) {
		FileManager fl = new FileManager("./assets/logs/");
		List<String> fls = fl.listFiles("", "");

		//LOGGING STUFF
		String[] parts = e.getArgs().split(";");
		if(parts.length==2) {
			if(parts[0].equalsIgnoreCase("warn")) {
				Bumblebot.logger.warn(parts[1]);
				UsrMsgUtil.sendVEMessage("**LogWarn** with the message```"+parts[1]+"```", e.getChannel());
				return;
			}
			if(parts[0].equalsIgnoreCase("debug")) {
				Bumblebot.logger.debug(parts[1]);
				UsrMsgUtil.sendVEMessage("**LogDebug** with the message```"+parts[1]+"```", e.getChannel());
				return;
			}
			if(parts[0].equalsIgnoreCase("info")) {
				Bumblebot.logger.info(parts[1]);
				UsrMsgUtil.sendVEMessage("**LogInfo** with the message```"+parts[1]+"```", e.getChannel());
				return;
			}
			UsrMsgUtil.sendEMessage("Logger levels must be any of the following:```warn, debug, info```", e.getChannel());
			return;
		}else if(e.getArgs().equalsIgnoreCase("latest")) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-MM-yyyy");
			LocalDateTime now = LocalDateTime.now();
			String date = dtf.format(now);
			if(fls.size() > 0) {
				for (String fl1 : fls) {
					if (date.equals(fl1.replace(".log", ""))) {
						String contents = FileManager.readFile(fl1);
						if(contents.length() > 1900) {
							UsrMsgUtil.sendVEMessage("[File contents]("+OtherUtil.postToHaste(contents)+")", e.getChannel());
						}else{
							UsrMsgUtil.sendVEMessage("Contents of the log file:\n```" +contents+"```", e.getChannel());
						}
					}
				}
			}else{
				e.reply("No log files were found!");
			}
			return;
		}
		
		String rgx = "(([1-9][0-9])|(0?[1-9]))d(([1-9][0-9])|(0?[1-9]))m|(([1-9][0-9])|(0?[1-9]))m(([1-9][0-9])|(0?[1-9]))d";
		if(e.getArgs().replace(" ", "").matches(rgx)) {
			String contents = FileManager.readFile(getDate(e.getArgs()));
			if(contents.length() > 1900) {
				UsrMsgUtil.sendVEMessage("[File contents]("+OtherUtil.postToHaste(contents)+")", e.getChannel());
			}else{
				UsrMsgUtil.sendVEMessage("Contents of the log file:\n```" +contents+"```", e.getChannel());
			}
			return;
		}
		
		
		//LISTING LOG FILES
		StringBuilder sb = new StringBuilder();
		if(fls.size() > 0) {
			sb.append("**__List of logs__:**\n");
			for(int i = 0; i < fls.size(); i++) {
				sb.append("**").append(i + 1).append(".** ").append(fls.get(i)).append("\n");
			}
		}else{
			sb.append("No log files were found!");
		}
		UsrMsgUtil.sendVEMessage(sb.toString(), e.getChannel());
	}
	
	private String getDate(String value) {
		String[] d = value.split("[d]");
		String[] m = value.split("[m]");

		return m[0].replace(d[0]+"d", "").replace(" ", "")+"-"+d[0].replace(m[0]+"m", "").replace(" ", "")+"-2018.log";
	}
}
