package commands.owner;


import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

public class EvalCmd extends Command {

    public EvalCmd() {
        this.name = "eval";
        this.help = "Testing some stuff";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.category = Bumblebot.Owner;
        this.hidden = true;
    }
    
    @Override
    protected void execute(CommandEvent e) {
    	String parse = "\nimport java.awt.Color\n" +
				"import net.dv8tion.jda.core.*\nimport utility.ConfigUtil as con\nimport utility.OtherUtil as othr\nimport utility.core.UsrMsgUtil as umt"
				+ "\nimport org.json.JSONArray\n" + "import org.json.JSONObject\n"
				+ e.getArgs();
    	try{
			Binding bd = new Binding();
			bd.setProperty("e", e);
			bd.setProperty("channel", e.getChannel());
			bd.setProperty("guild", e.getGuild());
			bd.setProperty("jda", e.getJDA());
			GroovyShell g = new GroovyShell(bd);
			String ll = g.evaluate(parse) + "";
			if(!(ll.equals("null"))) {
				e.reply(ll);
			}	
		}catch (Exception ex) {
			e.reply(new EmbedBuilder().addField("What you tried to do", "```groovy\n"+parse+"```", false)
					.addField("Exception", "```groovy\n"+ex.toString()+"```", false).setColor(Color.decode(ConfigUtil.getHex())).build());
		}
    }
    
}