package commands.info;

import java.awt.Color;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class UserInfoCmd extends Command {
	
	public UserInfoCmd() {
		this.name = "userinfo";
		this.aliases = new String[] {"usr", "user", "ui"};
		this.help = "Returns with information about a certain user";
		this.arguments = "<@user> | <ID> {} @BumbleCore";
		this.category = Bumblebot.Info;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		try {
			User user;
			if(e.getMessage().getMentionedUsers().size() > 0) {
				user = e.getMessage().getMentionedUsers().get(0);
			}else if(!e.getArgs().isEmpty()) {
				user = e.getJDA().retrieveUserById(e.getArgs()).complete();
			}else{
				user = e.getAuthor();
			}
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setThumbnail(user.getEffectiveAvatarUrl());
			eb.addField("Username", user.getName() + "#" + user.getDiscriminator(), true);
			eb.addField("ID", user.getId(), true);
			eb.addField("Created on", user.getCreationTime().atZoneSameInstant(ZoneId.of("GMT+0")).format(DateTimeFormatter.ofPattern("dd MMM, yyyy")), true);
			if(UsrMsgUtil.isInGuild(e.getGuild(), user.getId())) {
				String status = e.getGuild().getMember(user).getOnlineStatus().toString().replace("_", " ");
				
			   	eb.setColor(e.getGuild().getMember(user).getColor());
				eb.addField("Joined on", e.getGuild().getMember(user).getJoinDate().atZoneSameInstant(ZoneId.of("GMT+0")).format(DateTimeFormatter.ofPattern("dd MMM, yyyy")), true);
			   	eb.addField("Status", status.substring(0,1).toUpperCase() + status.substring(1).toLowerCase(), true);
			   
			   	Game gm =  e.getGuild().getMember(user).getGame();
			   	eb.addField("Game", (gm != null) ? gm.getName() : "None", true);
				
				
				List<Role> getRl = e.getGuild().getMember(user).getRoles();
				int roles = getRl.size() - 2;
				if(getRl.size() == 1) {
		    		eb.addField("Roles [" + getRl.size() + "]", getRl.get(0).getName(), true);
			   	}else if(getRl.size()  == 2) {
			   		 eb.addField("Roles [" + getRl.size() + "]", getRl.get(0).getName() + ", " + getRl.get(1).getName() , true);
			   	}else if(getRl.size() > 2) {
			   		 eb.addField("Roles [" + getRl.size() + "]", getRl.get(0).getName() + ", " + getRl.get(1).getName() + " ..and **" + roles + "** more", false);
			   	}else {
					getRl.size();
					eb.addField("Roles", "None" , true);
				}
				
				List<Permission> getPr = e.getGuild().getMember(user).getPermissions();
				int permss = getPr.size() - 3;
				if(getPr.size() == 1) {
		    		eb.addField("Permissions [" + getPr.size() + "]", 
		    				getPr.get(0).getName().substring(0,1).toUpperCase() + getPr.get(0).getName().substring(1).toLowerCase(), true);
			   	}else if(getPr.size()  == 2) {
			   		 eb.addField("Permissions [" + getPr.size() + "]", 
			   				 getPr.get(0).getName().substring(0,1).toUpperCase() + 
			   				 getPr.get(0).getName().substring(1).toLowerCase()
			   				 + ", " + getPr.get(1).getName().substring(0,1).toUpperCase() + getPr.get(1).getName().substring(1).toLowerCase() , true);
			   	}else if(getPr.size() > 2) {
			   		 eb.addField("Permissions [" + getPr.size() + "]", 
			   				 getPr.get(0).getName().substring(0,1).toUpperCase() + getPr.get(0).getName().substring(1).toLowerCase()
			   				 + ", " + getPr.get(1).getName().substring(0,1).toUpperCase() + getPr.get(1).getName().substring(1).toLowerCase()
			   				 + ", " + getPr.get(2).getName().substring(0,1).toUpperCase() + getPr.get(2).getName().substring(1).toLowerCase()
			   				 + " ..and **" + permss + "** more", false);
			   	}else {
					getPr.size();
					eb.addField("Permissions", "None" , true);
				}
				
			}
			
			eb.setColor(Color.decode(ConfigUtil.getHex()));			
			eb.setFooter("Requested by " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), e.getAuthor().getEffectiveAvatarUrl());
			e.reply(eb.build());
		}catch (Exception ex){
			if(ex instanceof NumberFormatException) {
				UsrMsgUtil.sendEMessage("Please provide a valid ID", e.getChannel());
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
