package xyz.bumbleboss.bumblebot;

import org.json.simple.JSONObject;

public class Constants {

  public static String TOKEN = Config.getConfigVal("token").toString();

  public static String HEX = Config.getConfigVal("hex").toString();

  public static String PREFIX = Config.getConfigVal("prefix").toString();

  public static String SERVER_ID = getServer().get("id").toString();

  public static JSONObject getServer() {
    return (JSONObject) Config.getConfigVal("server");
  }
}