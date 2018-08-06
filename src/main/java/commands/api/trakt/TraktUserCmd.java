package commands.api.trakt;

import java.awt.Color;
import java.net.SocketTimeoutException;
import java.util.Objects;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.User;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.enums.Extended;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class TraktUserCmd extends Command {

	public TraktUserCmd() {
		this.name = "trakt";
		this.help = "Get profile of a Trakt.tv user!";
		this.arguments = "<username> {} Bumbleboss";
		this.aliases = new String[] {"traktuser"+"trusr"};
		this.category = Bumblebot.API;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		if(e.getArgs().isEmpty()) {
			UsrMsgUtil.sendEMessage("You need to provide a username!", e.getChannel());
			return;
		}
		
		TraktV2 trakt = ConfigUtil.trakt;
	    UserSlug usr = new UserSlug(e.getArgs());
	    
	    try { 
			User user = trakt.users().profile(usr, Extended.FULL).execute().body();
			if(Objects.requireNonNull(user).isPrivate) {
				e.reply(new EmbedBuilder()
						.setAuthor(user.username, "https://trakt.tv/users/"+user.username, null).setDescription("This user is private.")
						.setColor(Color.decode("#ED1C24"))
						.build());
			}else{
				e.reply(new EmbedBuilder()
						.setAuthor(user.username, "https://trakt.tv/users/"+user.username, user.images.avatar.full)
						.setThumbnail(user.images.avatar.full)
						.addField("About", user.about == null ? "N/A" : user.about, false)
						.addField("Other", "Age: "+ (user.age == 0 ? "N/A" : user.age) 
								+"\nLocation: " + (user.location == null ? "N/A" : user.location)
								+"\nJoin date: " + OtherUtil.getDate(user.joined_at.toString()), true)
						.setColor(Color.decode("#ED1C24"))
						.setFooter("Provided by Trakt.tv", null)
						.build());
			}
		} catch (Exception ex) {
			if(ex instanceof SocketTimeoutException) {
				e.reply("I just got a timeout! Try again in a few minutes.");
			}else if(ex instanceof NullPointerException) {
				e.reply("There is no user that goes by that name.");
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
