package xyz.bumbleboss.bumblebot;

import org.json.simple.*;
import xyz.bumbleboss.core.*;
import xyz.bumbleboss.exceptions.validateFailedException;

import java.util.Hashtable;

public class Config {
  
  private static final String path = "./assets/configs/beta_config.json";
  private static final String jsonFile = FileManager.readFile(path);
  private static JSONObject jsonData = (JSONObject) Util.getJSON(jsonFile);
  private static String[] requiredKeys = {"developersId", "token", "prefix", "ownerId", "hostId", "server", "hex"};
  private final Hashtable validConfigFormat = new Hashtable();

  public static Object getConfigVal(String key) {
    return jsonData.get(key);
  }

  public static void validateConfig() throws validateFailedException {
    Util.validateJson(jsonData, path, requiredKeys);
  }
}