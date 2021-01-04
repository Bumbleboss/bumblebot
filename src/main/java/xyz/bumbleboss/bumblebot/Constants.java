package xyz.bumbleboss.bumblebot;

import org.json.JSONObject;
import org.json.JSONArray;

import xyz.bumbleboss.core.Util;

public class Constants {

  public static final String TOKEN = Config.getConfigVal("token").toString();

  public static final String COLOR = Config.getConfigVal("hex").toString();

  public static final String PREFIX = Config.getConfigVal("prefix").toString();

  public static final String WEBHOOK = Config.getConfigVal("webhook").toString();

  public static final String SERVER_ID = getServer().get("id").toString();

  public static final String OWNER_ID = Config.getConfigVal("ownerId").toString();

  public static final String HOST_ID = Config.getConfigVal("hostId").toString();

  public static final String[] DEVS_ID = Util.JSONArrayTo((JSONArray) Config.getConfigVal("developersId"), String.class, Object::toString);

  public static JSONObject getServer() {
    return (JSONObject) Config.getConfigVal("server");
  }
}