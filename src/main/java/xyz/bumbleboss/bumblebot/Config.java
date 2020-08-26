package xyz.bumbleboss.bumblebot;

import org.json.simple.*;

import xyz.bumbleboss.core.*;
import xyz.bumbleboss.exceptions.dataValueMissingException;

public class Config {
  
  private static String path = "./assets/beta_config.json";
  private static String jsonFile = FileManager.readFile(path);
  private static JSONObject jsonData = (JSONObject) Util.getJSON(jsonFile);

  public static Object getConfigVal(String key) {
    return jsonData.get(key);
  }

  // Checks to ensure all required keys are available.
  public static Object initConfig(String[] requiredKeys) throws dataValueMissingException {
    return Util.validateJson(jsonData, path, requiredKeys);
  }
}