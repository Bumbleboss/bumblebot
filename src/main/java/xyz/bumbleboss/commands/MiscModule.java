package xyz.bumbleboss.commands;

import java.util.Objects;

import com.jockie.bot.core.argument.Argument;
import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;

import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;
import xyz.bumbleboss.core.api.UrbanAPI;

@Module
public class MiscModule {

  @Command(value="invite", description="Invitation link to BumbleCore's server")
  public void invite(CommandEvent e) {
    String msg;
    
    if (Objects.equals(Constants.SERVER_ID, e.getGuild().getId())) {
      msg = "Invite people to this server and let them join the booblings!";
    } else {
      msg = "Hope I see you there!";
    }
    
    Util.respond(e, true, msg);
  }

  @Command(value="define", description="Meaning of a word directly from Urban Dictionary")
  public void define(CommandEvent e, @Argument("word") String word) {
    //Util.respond(e, true, new UrbanAPI(word).working());
  }
}