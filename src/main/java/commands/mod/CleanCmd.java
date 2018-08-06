package commands.mod;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import utility.ConfigUtil;
import utility.core.UsrMsgUtil;

public class CleanCmd extends Command {
    private final Pattern QUOTES_PATTERN = Pattern.compile("\"(.*?)\"", Pattern.DOTALL);
    private final Pattern MENTION_PATTERN = Pattern.compile("<@!?(\\d{17,22})>");
    private final Pattern ID_PATTERN = Pattern.compile("(?:^|\\s)(\\d{17,22})(?:$|\\s)");
    private final Pattern NUM_PATTERN = Pattern.compile("(?:^|\\s)(\\d{1,4})(?:$|\\s)");
    private final String week2limit = " Messages older than 2 weeks cannot be cleaned.";
    private final String noparams = "You need to provide one of the parameters!\n"
                + " **num** - Number of messages to delete; between 2 and 1000\n"
                + " **user** - Cleans messages only from the mentioned user\n"
                + " **userId** - Cleans messages only from the userId\n"
                + " **quotes** - Cleans messages containing the text in quotes";    
    private final ScheduledExecutorService threadpool;
    
    public CleanCmd(ScheduledExecutorService threadpool) {
        this.threadpool = threadpool;
    	this.category = Bumblebot.Mod;
        this.name = "clean";
        this.arguments = "<parameters> {} 20";
        this.help = noparams;
        this.aliases = new String[] {"prune", "delete", "remove"};
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }
    
    @Override
    protected void execute(CommandEvent e) {
        if(e.getArgs().isEmpty()){
        	UsrMsgUtil.sendEMessage(noparams, e.getChannel());
            return;
        }
        
        int num = -1;
        List<String> quotes = new LinkedList<>();
        List<String> ids = new LinkedList<>();
        String parameters = e.getArgs();
        
        Matcher m = QUOTES_PATTERN.matcher(parameters);
        while(m.find())
            quotes.add(m.group(1).trim().toLowerCase());
        parameters = parameters.replaceAll(QUOTES_PATTERN.pattern(), " ");
        
        m = MENTION_PATTERN.matcher(parameters);
        while(m.find())
            ids.add(m.group(1));
        parameters = parameters.replaceAll(MENTION_PATTERN.pattern(), " ");
        
        m = ID_PATTERN.matcher(parameters);
        while(m.find())
            ids.add(m.group(1));
        parameters = parameters.replaceAll(ID_PATTERN.pattern(), " ");
        
        m = NUM_PATTERN.matcher(parameters);
        if(m.find())
            num = Integer.parseInt(m.group(1));

        boolean all = quotes.isEmpty() && ids.isEmpty();
        
        if(num==-1){
            if(all){
            	UsrMsgUtil.sendEMessage(noparams, e.getChannel());
                return;
            }else
                num=100;
        }
        if(num>100 || num<2){
        	UsrMsgUtil.sendEMessage("I can only delete messages from 2-100 messages!", e.getChannel());
            return;
        }
        
        int val2 = num+1;
        threadpool.submit(() -> {
            int val = val2;
            List<Message> msgs = new LinkedList<>();
            MessageHistory mh = e.getChannel().getHistory();
            OffsetDateTime earliest = e.getMessage().getCreationTime().minusWeeks(2).plusMinutes(1);
            while(val>100){
                msgs.addAll(mh.retrievePast(100).complete());
                val-=100;
                if(msgs.get(msgs.size()-1).getCreationTime().isBefore(earliest)){
                    val=0;
                    break;
                }
            }
            if(val>0)
                msgs.addAll(mh.retrievePast(val).complete());

            msgs.remove(e.getMessage());
            boolean week2 = false;
            List<Message> del = new LinkedList<>();
            for(Message msg : msgs){
                if(msg.getCreationTime().isBefore(earliest)){
                    week2 = true;
                    break;
                }
                if(all || ids.contains(msg.getAuthor().getId())){
                    del.add(msg);
                    continue;
                }
                String lowerContent = msg.getContentRaw().toLowerCase();
                if(quotes.stream().anyMatch(lowerContent::contains)){
                    del.add(msg);
                }
            }

            if(del.isEmpty()){
            	UsrMsgUtil.sendEMessage("There were no messages to clean!"+(week2?week2limit:""), e.getChannel());
                return;
            }
            String reason = e.getAuthor().getName()+"#"+e.getAuthor().getDiscriminator()+" [clean]: "+e.getMessage().getContentRaw();
            if(reason.length()>512)
                reason = reason.substring(0,512);
            try{
                int index = 0;
                while(index < del.size()){
                    if(index+100>del.size())
                        if(index+1==del.size())
                            del.get(del.size()-1).delete().reason(reason).complete();
                        else
                            e.getTextChannel().deleteMessages(del.subList(index, del.size())).complete();
                    else
                        e.getTextChannel().deleteMessages(del.subList(index, index+100)).complete();
                    index+=100;
                }
            }catch(Exception ex){
            	UsrMsgUtil.sendEMessage("Failed to delete "+del.size()+" messages.", e.getChannel());
                return;
            }
            e.getTextChannel().sendMessage(new EmbedBuilder().setAuthor(e.getAuthor().getName(), null, e.getAuthor().getAvatarUrl())
            		.setDescription("Deleted **"+del.size()+"** messages!"+(week2?week2limit:""))
            		.setColor(Color.decode(ConfigUtil.getHex()))
            		.build()).queue(m1-> m1.delete().queueAfter(2, TimeUnit.SECONDS));
        });
    }
}