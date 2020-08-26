package xyz.bumbleboss.bumblebot;

import org.json.simple.*;

import xyz.bumbleboss.core.*;

import java.util.ArrayList;

public class Config {
  
  private static String path = "./assets/beta_config.json";
  private static String jsonFile = FileManager.readFile(path);
  private static JSONObject jsonData = (JSONObject) Util.getJSON(jsonFile);

  public static Object getConfigVal(String key) {
    return jsonData.get(key);
  }

  // Checks to ensure all required keys are available.
  public static Object initConfig(String[] requiredKeys) throws configValueMissingException {
    ArrayList<String> success = new ArrayList<String>();
    ArrayList<String> failure = new ArrayList<String>();
    StringBuffer output = new StringBuffer();
    for (int i = 0; i < requiredKeys.length; i++){
      if (jsonData.containsKey(requiredKeys[i])) {
        success.add(requiredKeys[i]);
      }else{
        failure.add(requiredKeys[i]);
      }
    }
    if (failure.size() > 0){
      output.append("Found required keys: ");
      for(int i = 0; i < success.size(); i++) {
        output.append(success.get(i));
        if (i < success.size() - 1) {
          output.append(", ");
        }
      }
      output.append("\n Missing required keys: ");
      for(int i = 0; i < failure.size(); i++){
        output.append(failure.get(i));
        if(i < failure.size() - 1){
          output.append(", ");
        }
      }
      throw new configValueMissingException(output.toString());
    }else{
      output.append("All required configuration settings found");
        }
    return output.toString();
  }
}