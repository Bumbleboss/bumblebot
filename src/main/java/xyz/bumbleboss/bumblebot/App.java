package xyz.bumbleboss.bumblebot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import com.jockie.bot.core.command.impl.*;

import org.json.simple.JSONArray;

import xyz.bumbleboss.core.Util;

public class App {
  
  public static JDA jda;
  public static String TOKEN = Config.getConfigVal("token").toString();
  public static JSONArray DEVS = (JSONArray) Config.getConfigVal("devs");
  public static String PREFIX = Config.getConfigVal("prefix").toString();

  public static void main(String[] args) {    
    try {
      CommandListener listener = new CommandListener();
      
      listener.addCommandStore(CommandStore.of("xyz.bumbleboss.commands"));
      listener.addDevelopers(Util.toArrayLong(DEVS));
      listener.setDefaultPrefixes(PREFIX);

      jda = new JDABuilder().setToken(TOKEN).addEventListeners(listener).build();
    } catch (LoginException e) {
      e.printStackTrace();
    }
  }
}
