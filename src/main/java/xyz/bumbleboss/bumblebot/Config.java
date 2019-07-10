package xyz.bumbleboss.bumblebot;

import org.json.simple.parser.*;
import org.json.simple.*;

import xyz.bumbleboss.core.*;

public class Config {
  
  private static String path = "./assets/beta_config.json";
  private static String jsonData = FileManager.readFile(path);

  public static Object getConfigVal(String key) {
    JSONParser jsonParser = new JSONParser();
    JSONObject js = null;

    try {
      Object obj = jsonParser.parse(jsonData);
      js = (JSONObject) obj;
    }catch(Exception e) {
      e.printStackTrace();
    }
    return js.get(key);
  }
}