package xyz.bumbleboss.commands;

import java.awt.Color;

import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;

import net.dv8tion.jda.api.EmbedBuilder;

import xyz.bumbleboss.bumblebot.Constants;

@Module
public class MiscModule {

  @Command(value="invite", description="Invitation link to BumbleCore's server")
  public void invite(CommandEvent e) {
    String msg = "";
    
    if (Constants.SERVER_ID == e.getGuild().getId()) {
      msg = "Invite people to this server and let them join the booblings!";
    } else {
      msg = "Hope I see you there!";
    }
    
    e.reply(new EmbedBuilder()
      .setDescription(msg+"\nInvite link: https://discord.gg/7PCdKYN")
      .setColor(Color.decode(Constants.COLOR))
      .build())
    .queue();
  }
  
}