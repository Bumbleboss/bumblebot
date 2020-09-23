package xyz.bumbleboss.bumblebot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import com.jockie.bot.core.command.impl.*;

import xyz.bumbleboss.exceptions.validateFailedException;

import java.util.EnumSet;

public class Bot {
  
  public static JDA jda;

  public static void main(String[] args) {    
    try {
      Config.validateConfig();
    } catch (validateFailedException e) {
      e.printStackTrace();
      System.exit(1);
    }
    
    try {
      CommandListener listener = new CommandListener();
      
      listener.addCommandStores(CommandStore.of("xyz.bumbleboss.commands"));
      listener.addDevelopers(Constants.DEVS_ID);
      listener.setDefaultPrefixes(Constants.PREFIX);

      jda = JDABuilder.create(Constants.TOKEN, EnumSet.allOf(GatewayIntent.class)).addEventListeners(listener).build();
      
    } catch (LoginException | NullPointerException e) {
      e.printStackTrace();
    }
  }
}
