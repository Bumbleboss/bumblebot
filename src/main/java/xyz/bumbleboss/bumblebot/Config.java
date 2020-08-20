package xyz.bumbleboss.bumblebot;

import org.json.simple.*;

import xyz.bumbleboss.core.*;

public class Config {
  
  private static String path = "./assets/beta_config.json";
  private static String jsonData = FileManager.readFile(path);

  public static Object getConfigVal(String key) throws configValueMissingException {
    JSONObject js = (JSONObject) Util.getJSON(jsonData);
    Object retval = js.get(key);
    if(null == retval){
      throw new configValueMissingException(key);
    } else {
      return js.get(key);
    }
  }
}