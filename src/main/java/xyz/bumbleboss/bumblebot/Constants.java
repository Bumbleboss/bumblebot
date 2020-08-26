package xyz.bumbleboss.bumblebot;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import xyz.bumbleboss.core.Util;

public class Constants {

  public static String TOKEN = Config.getConfigVal("token").toString();

  public static String COLOR = Config.getConfigVal("hex").toString();

  public static String PREFIX = Config.getConfigVal("prefix").toString();

  public static String SERVER_ID = getServer().get("id").toString();

  public static String OWNER_ID = Config.getConfigVal("ownerId").toString();

  public static String HOST_ID = Config.getConfigVal("hostId").toString();

  public static String[] DEVS_ID = Util.JSONArrayTo((JSONArray) Config.getConfigVal("developersId"), String.class, Object::toString);

  public static JSONObject getServer() {
    return (JSONObject) Config.getConfigVal("server");
  }
}