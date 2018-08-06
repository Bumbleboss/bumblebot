package main;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import utility.core.FileManager;

public class Config {

	private static final String jsonData = FileManager.readFile("./assists/config.json");
	private static final JSONObject inf = new JSONObject(jsonData);
    private JSONObject configObject;

	public Config() {
        JSONObject object = inf;	
        
        if (object.has("token") && object.has("prefix") && object.has("ownerid")) {
            configObject = object;
        }else{ 
            System.err.println("A value was missing in the config file!");
            System.exit(1);
        }
	}
	
	public String getValue(String key) {
		return configObject == null ? null : configObject.get(key).toString();
    }
	
	public JSONObject getJSONObject(String key) {
		return configObject == null ? null : configObject.getJSONObject(key);
	}
	
	public JSONArray getJSONArray(String key) {
		return configObject == null ? null : configObject.getJSONArray(key);
	}
	
	public void setValue(String key, String value) {
		JSONObject object = configObject;
		object.put(key, value);
		
		try (FileWriter file = new FileWriter("./config.json")) {
			file.write(object.toString());
        } catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
