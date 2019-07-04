package xyz.bumbleboss.bumblebot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class App implements EventListener {
  public static JDA jda;
  public static String TOKEN = "";

  public static void main(String[] args) {
    try {
      jda = new JDABuilder()
      .setToken(TOKEN)
      .addEventListeners(new App())
      .build();
    } catch (LoginException e) {
      e.printStackTrace();
    }
  }

  public String getBotName() {
    return jda.getSelfUser().getName();
  }

  @Override
  public void onEvent(GenericEvent event) {
    if (event instanceof ReadyEvent) {
      System.out.println(getBotName());
    }
  }
}
