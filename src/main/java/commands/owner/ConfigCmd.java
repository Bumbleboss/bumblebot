package commands.owner;


import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class ConfigCmd extends Command {
	
	public ConfigCmd() {
		this.name = "config";
		this.help = "Changing the variables in the config file";
		this.ownerCommand = true;
		this.guildOnly = false;
		this.category = Bumblebot.Owner;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent e) {
		String[] arg = e.getArgs().split(";");
		
		try {
			String vl = arg[0];
			String cvl = arg[1].replace(" ", "").replace("_", " ");
			String svl = null;
			Boolean isValid = true;
			
			if(vl.equals(VALUES.HELPWORD.getValue())) {
				svl = ConfigUtil.getHelpWord();
				ConfigUtil.setHelpWord(cvl);
			}else if(vl.equals(VALUES.HEX.getValue())) {
				svl = ConfigUtil.getHex();
				ConfigUtil.setHex(cvl);
			}else if(vl.equals(VALUES.PREFIX.getValue())) {
				svl = ConfigUtil.getPrefix();
				ConfigUtil.setPrefix(cvl);
			}else{
				isValid = false;
			}
				
			if(isValid) {
				UsrMsgUtil.sendVEMessage("Changed the json value **"+vl+"** from **"+svl+"** to **"+cvl+"**", e.getChannel());
			}else{
				UsrMsgUtil.sendEMessage("You have not given any proper values to be changed.", e.getChannel());
			}	
		}catch(Exception ex) {
			if(ex instanceof ArrayIndexOutOfBoundsException) {
				e.reply("You need to put the json field and it's updated value!");
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}

	public enum VALUES {
		HELPWORD("help"),
		HEX("hex"),
		PREFIX("prefix");
			
		final String value;
		VALUES(String s) {
			this.value = s;
		}
		
		String getValue() {
			return value;
		}
	}
}
