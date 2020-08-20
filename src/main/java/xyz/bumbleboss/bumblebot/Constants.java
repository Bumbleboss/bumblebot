package xyz.bumbleboss.bumblebot;

import org.json.simple.JSONObject;

public class Constants {

  public static String TOKEN;
  public static String HEX;
  public static String PREFIX;
  public static String SERVER_ID;

  static {
    try {
      TOKEN = Config.getConfigVal("token").toString();
      HEX = Config.getConfigVal("hex").toString();
      PREFIX = Config.getConfigVal("prefix").toString();
      SERVER_ID = getServer().get("id").toString();
    } catch (configValueMissingException e) {
    }
  }

  public static JSONObject getServer() throws configValueMissingException {
    return (JSONObject) Config.getConfigVal("server");
  }
}