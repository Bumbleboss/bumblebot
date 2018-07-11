package commands.family.marriage;

import java.awt.Color;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import commands.family.Marriage;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class MarriageInfoCmd extends Command {

	public MarriageInfoCmd() {
		this.name = "minfo";
		this.help = "Check the marriage status of yourself or a mentioned user.";
		this.category = Bumblebot.Marriage;
	}
	@Override
	protected void execute(CommandEvent e) {
		Marriage mrg = new Marriage();
		EmbedBuilder eb = new EmbedBuilder();	
		String user;
		if(e.getMessage().getMentionedUsers().size() > 0) {
			user = e.getMessage().getMentionedUsers().get(0).getId();
		}else{
			user = e.getAuthor().getId();
		}
		
		boolean isInGuild = UsrMsgUtil.isInGuild(e.getGuild(), mrg.getPartner(user));
		
		try {
			eb.setColor(Color.decode(ConfigUtil.getHex()));
			eb.setAuthor("Marriage info", null, e.getJDA().getUserById(user).getEffectiveAvatarUrl());	
			
			//IF MENTIONED USER/AUTHOR IS MY BOT
			if(user.equals(e.getJDA().getSelfUser().getId())) {
				eb.setDescription("That'e a secret!");
			//IF MENTIONED USER/AUTHOR IS A BOT
			}else if(e.getJDA().getUserById(user).isBot()) {
				eb.setDescription("You cannot check marriage status of a bot.");
			//IF MENTIONED USER/AUTHOR IS ALREADY MARRIED	
			}else if(mrg.isMarried(user)) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				ZonedDateTime time = ZonedDateTime.parse(mrg.getMarriageDate(user), formatter.withZone(ZoneId.of("UTC+0")));
				//IF MENTIONED USER/AUTHOR IS THE PROPOSER
				if(mrg.isProposing(user)) {
					//IF USER IS THE AUTHOR
					if(user.equals(e.getAuthor().getId())) {
						//IF PARTNER IS ON SERVER
						if(isInGuild) {
							eb.setDescription("You are married to " +e.getJDA().getUserById(mrg.getPartner(user)).getAsMention() 
									+", with "+e.getAuthor().getAsMention()+" as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}else{
							eb.setDescription("You are married to **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**,"
									+ " with "+e.getAuthor().getAsMention()+" as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}
					}else{
						//IF PARTNER IS ON SERVER
						if(isInGuild) {
							eb.setDescription(e.getJDA().getUserById(user).getAsMention() +" is married to "
									+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention() +", with "+e.getJDA().getUserById(user).getAsMention()+" as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}else{
							eb.setDescription(e.getJDA().getUserById(user).getAsMention() +" is married to **"
									+ UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**, with "+e.getJDA().getUserById(user).getAsMention()+" as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}
					}
				//IF MENTIONED USER IS THE PROPOSED TO
				}else if(mrg.isProposedTo(user)) {	
					//IF USER IS THE AUTHOR
					if(user.equals(e.getAuthor().getId())) {
						//IF PARTNER IS ON SERVER
						if(isInGuild) {
							eb.setDescription("You are married to " +e.getJDA().getUserById(mrg.getPartner(user)).getAsMention() 
									+", with "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention()+" as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}else{
							eb.setDescription("You are married to **" +UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user)) 
									+"**, with **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"** as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}
					}else {
						//IF PARTNER IS ON SERVER
						if(isInGuild) {
							eb.setDescription(e.getJDA().getUserById(user).getAsMention() + " is married to " + e.getJDA().getUserById(mrg.getPartner(user)).getAsMention()
									+", with " + e.getJDA().getUserById(mrg.getPartner(user)).getAsMention() + " as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}else{
							eb.setDescription(e.getJDA().getUserById(user).getAsMention() + " is married to **"+UsrMsgUtil.getUserSet(e.getJDA(),mrg.getPartner(user))
									+"**, with **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"** as the proposer.");
							eb.setFooter("Married on ", null);
							eb.setTimestamp(time.toInstant());
						}
					}
				}
			//IF MENTIONED USER IS STILL PROPOSING	
			}else if(mrg.isProposing(user)) {
				//IF USER IS THE AUTHOR
				if(user.equals(e.getAuthor().getId())) {
					//IF PARTNER IS ON SERVER
					if(isInGuild) {
						eb.setDescription("You are proposing to " +e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
					}else{
						eb.setDescription("You are proposing to **" +UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user)) +"**");
					}
				}else{
					//IF PARTNER IS ON SERVER
					if(isInGuild) {
						eb.setDescription(e.getJDA().getUserById(user).getAsMention() + " is proposing to " +e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
					}else{
						eb.setDescription(e.getJDA().getUserById(user).getAsMention() + " is proposing to **" +UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user)) +"**");
					}
				}
			//IF MENTIONED USER IS STILL BEING PROPOSED TO
			}else if(mrg.isProposedTo(user)) {
				if(user.equals(e.getAuthor().getId())) {
					//IF PARTNER IS ON SERVER
					if(isInGuild) {
						eb.setDescription("You are being proposed to by " +e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
					}else{
						eb.setDescription("You are being proposed to by **" +UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user)) +"**");
					}
				}else{
					//IF PARTNER IS ON SERVER
					if(isInGuild) {
						eb.setDescription(e.getJDA().getUserById(user).getAsMention() + " is being proposed to by " +e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
					}else{
						eb.setDescription(e.getJDA().getUserById(user).getAsMention() + " is being proposed to by **" +UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user)) +"**");
					}
				}
			}else if(user.equals(e.getAuthor().getId())) {
					eb.setDescription("You are not married. Go ahead and propose to your loved one!");
				}else{
					eb.setDescription(e.getJDA().getUserById(user).getAsMention()+" is not married. Be their cupid and tell them to propose!");
				}
			
		}catch(Exception ex) {
			if(ex instanceof NullPointerException) {
				eb.setDescription("Requested user's partner is no longer on any servers with the bot");
			}else{
				eb.setDescription("Error");
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}	
		}
		e.reply(eb.build());
	}
}
