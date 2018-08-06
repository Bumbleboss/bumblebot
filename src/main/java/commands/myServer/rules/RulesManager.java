package commands.myServer.rules;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.core.FileManager;

class RulesManager {

	private static final String jsonData = FileManager.readFile("./assists/server_settings.json");
	private static final JSONObject role = new JSONObject(jsonData);
	private static final List<Object> list = getChannels().toList();

	
	public static JSONArray getChannels() {
		return role.getJSONArray("channels");
	}
	
	public static String getChannelName(int i) {
		return getChannels().getJSONObject(i).getString("name");
	}

	public static String getChannelId(int i) {
		return getChannels().getJSONObject(i).getString("id");
	}

	public static String getChannelDesc(String name) {
		for(int i = 0; i < list.size(); i++) {
			if(Objects.equals(name.toLowerCase(), getChannels().getJSONObject(i).getString("name"))) {
				return getChannels().getJSONObject(i).getString("description");
			}
		}
		return null;
	}
	
	public static String getChannelEmbed(String name) {
		for(int i = 0; i < list.size(); i++) {
			if(Objects.equals(name.toLowerCase(), getChannels().getJSONObject(i).getString("name"))) {
				return getChannels().getJSONObject(i).get("embed").toString();
			}
		}
		return null;
	}
	
	public static boolean matchChannel(String channel) {
		for(int i = 0; i < list.size(); i++) {
			if(Objects.equals(channel.toLowerCase(), getChannelName(i)) || Objects.equals(channel.toLowerCase(), getChannelId(i))) {
				return true;
			}
		}
		return false;
	}
}
