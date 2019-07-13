package xyz.bumbleboss.bumblebot;

import org.json.simple.*;

import xyz.bumbleboss.core.*;

public class Config {
  
  private static String path = "./assets/beta_config.json";
  private static String jsonData = FileManager.readFile(path);

  public static Object getConfigVal(String key) {
    JSONObject js = (JSONObject) Util.getJSON(jsonData);
    return js.get(key);
  }
}