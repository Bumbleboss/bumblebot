package xyz.bumbleboss.commands;

import java.awt.Color;

import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;

import net.dv8tion.jda.api.EmbedBuilder;

import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;

@Module
public class MiscModule {

  @Command(value="invite", description="Invitation link to BumbleCore's server")
  public void invite(CommandEvent e) {
    String msg = "";
    
    if(Constants.SERVER_ID == e.getGuild().getId()) {
      msg = "Inviting people here is always fun!";
    }else{
      msg = "Hope you enjoy your stay there!";
    }
    
    e.reply(new EmbedBuilder()
      .setDescription(msg+"\nInvite link: https://discord.gg/7PCdKYN")
      .setColor(Color.decode(Constants.HEX))
      .build())
    .queue();
  }

  /*
  @Command(value="hex", description="Get the hexcode from an image, url, and more")
  public void hex(CommandEvent e) {
    String hex = "#";
    String raw = hex.replace("#", "");

    e.reply(new EmbedBuilder()
      .setTitle(hex.toUpperCase())
      .setColor(Color.decode(hex)).setImage("http://placehold.it/1024x1024.png/"+raw+"/"+raw)
      .setFooter("Requested by " + Util.getFullName(e.getAuthor()), e.getAuthor().getAvatarUrl())
      .build())
    .queue();
  }
  */
}