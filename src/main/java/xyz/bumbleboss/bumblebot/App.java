package xyz.bumbleboss.bumblebot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import com.jockie.bot.core.command.impl.*;

import org.json.simple.JSONArray;

import xyz.bumbleboss.core.Util;

public class App {
  
  public static JDA jda;
  public static JSONArray DEVS = (JSONArray) Config.getConfigVal("devs");

  public static void main(String[] args) {    
    try {
      CommandListener listener = new CommandListener();
      
      listener.addCommandStore(CommandStore.of("xyz.bumbleboss.commands"));
      listener.addDevelopers(Util.toArrayLong(DEVS));
      listener.setDefaultPrefixes(Constants.PREFIX);

      jda = new JDABuilder().setToken(Constants.TOKEN).addEventListeners(listener).build();
    } catch (LoginException e) {
      e.printStackTrace();
    }
  }
}
