package xyz.bumbleboss.bumblebot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import com.jockie.bot.core.command.impl.*;

import org.json.simple.JSONArray;

import xyz.bumbleboss.core.Util;
import xyz.bumbleboss.exceptions.dataValueMissingException;

import java.util.EnumSet;

public class App {
  
  public static JDA jda;
  public static JSONArray DEVS = (JSONArray) Config.getConfigVal("devs");

  public static void main(String[] args) {
    try {
      String[] requiredKeys = {"devs","token","prefix","ownerId","hostId","server","hex"};
      System.out.println((String) Config.initConfig(requiredKeys));
    }catch (dataValueMissingException e) {
      e.printStackTrace();
      System.exit(1);
    }
    try {
      CommandListener listener = new CommandListener();
      
      listener.addCommandStore(CommandStore.of("xyz.bumbleboss.commands"));
      listener.addDevelopers(Util.toArrayLong(DEVS));
      listener.setDefaultPrefixes(Constants.PREFIX);

      jda = JDABuilder.create(Constants.TOKEN, EnumSet.allOf(GatewayIntent.class)).addEventListeners(listener).build();
      
    } catch (LoginException e) {
      e.printStackTrace();
    }
  }
}
