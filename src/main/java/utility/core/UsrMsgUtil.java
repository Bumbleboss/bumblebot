package utility.core;

import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import utility.ConfigUtil;

public class UsrMsgUtil {
	
	private static String fromArgument(String regex, String value) {
	    if(value.matches(regex)) {
	        Pattern pattern = Pattern.compile("[0-9]{15,}");
	        Matcher matcher = pattern.matcher(value);   
	        if(matcher.find()) {
	            return matcher.group(0);
	        }
	    }
	    return null;
	}
	
	public static boolean isInGuild(Guild guild, String input) {
		try {
			return guild.isMember(guild.getJDA().getUserById(input));
		}catch (NullPointerException | IllegalArgumentException ex) {
			return false;
		}
	}

	public static String getUserSet(JDA jda, String value) {
	    String id = fromArgument("(<@(!|)|)[0-9]{15,}(>|)", value);
	    if(id != null) {
	    	User usr = jda.retrieveUserById(id).complete();
	        return usr.getName()+"#"+usr.getDiscriminator();
	    }else{
	        return null;
	    }
	}


	private static boolean canNotTalk(TextChannel channel) {
        if (channel == null) return true;
        Member member = channel.getGuild().getSelfMember();
        return member == null
                || !member.hasPermission(channel, Permission.MESSAGE_READ)
                || !member.hasPermission(channel, Permission.MESSAGE_WRITE);
    }
	
	public static int[] getMembers(Guild guild) {
	    int[] all = new int[6];
	    
	    List<Member> members = guild.getMembers();
	    
	    for(int i = 0; i < members.size(); i++) {
	        Member member = members.get(i);
	        
	        if(member.getOnlineStatus().equals(OnlineStatus.ONLINE)) {
	            all[0] += 1;
	        }else if(member.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
	            all[1] += 1;
	        }else if(member.getOnlineStatus().equals(OnlineStatus.IDLE)) {
	            all[2] += 1;
	        }else if(member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)) {
	            all[3] += 1;
	        }
	        
	        if(member.getUser().isBot()) {
	            all[4] += 1;
	        }else{
	            all[5] += 1;
	        }
	    }   
	    return all;
	}
	
	public static void sendEMessage(String msg, MessageChannel channel) {
        if (channel instanceof TextChannel && canNotTalk((TextChannel) channel)) return;
        channel.sendMessage(new EmbedBuilder().setDescription(msg).build()).queue();
    }
	 
    public static void sendVEMessage(String msg, MessageChannel channel) {
        if (channel instanceof TextChannel && canNotTalk((TextChannel) channel)) return;
        channel.sendMessage(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setDescription(msg).build()).queue();
    }
    
    public static String stripFormatting(String s) {
    	StringBuilder sb = new StringBuilder();
    	for(char chr : s.toCharArray()) {
    	    if(Character.isAlphabetic(chr) || Character.isDigit(chr)) {
    	        sb.append(chr);
    	    }
    	}
    	return sb.toString();
	}
}
