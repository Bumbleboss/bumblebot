package commands.fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.OtherUtil;

public class ChooseCmd extends Command{

    public ChooseCmd(){
        this.name = "choose";
        this.help = "Makes a decision";
        this.arguments = "<item> | <item> ... {} Boob | Butt";
        this.category = Bumblebot.Fun;
    }
    
    @Override
    protected void execute(CommandEvent e){
        if(e.getArgs().isEmpty()){
            e.reply("You didn't give me any choices!");
            return;
        }
        
        String[] items = e.getArgs().split("\\| ");
        if(items.length==1) {
        	e.reply("You only gave me one option, **"+items[0]+"**");
        }else{
        	String[] replies = new String[] {"I choose", "I would pick", "I think I would go with", ":point_right:"};
        	e.reply(OtherUtil.getRandom(replies)+" "+"**"+OtherUtil.getRandom(items)+"**");
        }	
    }
}