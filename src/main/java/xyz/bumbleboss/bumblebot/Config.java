package xyz.bumbleboss.bumblebot;

import org.json.simple.*;

import xyz.bumbleboss.core.*;
import xyz.bumbleboss.exceptions.dataValueMissingException;

public class Config {
  
  private static String path = "./assets/beta_config.json";
  private static String jsonFile = FileManager.readFile(path);
  private static JSONObject jsonData = (JSONObject) Util.getJSON(jsonFile);
  private static String[] requiredKeys = {"devs","token","prefix","ownerId","hostId","server","hex"};

  public static Object getConfigVal(String key) {
    return jsonData.get(key);
  }

  public static Object validateConfig() throws dataValueMissingException {
    return Util.validateJson(jsonData, path, requiredKeys);
  }
}