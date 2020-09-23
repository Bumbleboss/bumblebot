package xyz.bumbleboss.bumblebot;

import org.json.simple.JSONObject;
import xyz.bumbleboss.core.FileManager;
import xyz.bumbleboss.core.Util;
import xyz.bumbleboss.exceptions.validateFailedException;

public class Config {
  
  private static final String path = "./assets/configs/beta_config.json";
  private static final String jsonFile = FileManager.readFile(path);
  private static final JSONObject jsonData = (JSONObject) Util.getJSON(jsonFile);
  private static final String[] requiredKeys = {"developersId", "token", "prefix", "ownerId", "hostId", "server", "hex"};

  public static Object getConfigVal(String key) {
    return jsonData.get(key);
  }

  public static void validateConfig() throws validateFailedException {
    Util.validateJson(jsonData, path, requiredKeys);
  }
}