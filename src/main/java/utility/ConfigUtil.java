package utility;

import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.trakt5.TraktV2;

import main.Config;
import org.jmusixmatch.MusixMatch;
import osuAPI.OsuAPI;
import urbanAPI.UrbanAPI;
import yandexAPI.YandexAPI;
import youTubeAPI.YouTubeAPI;

public class ConfigUtil {

	private static final Config config = new Config();
	public static final OsuAPI osu = new OsuAPI(getOSU());
	public static final TraktV2 trakt = new TraktV2(getTrakt());
	public static final YouTubeAPI yt = new YouTubeAPI(getYT());
	
	public static final UrbanAPI ub = new UrbanAPI(getUrban());
	public static final MusixMatch lyrc = new MusixMatch(getMusixMatch());
	public static final Tmdb tmdb = new Tmdb(getTMDB());
	public static final YandexAPI trns = new YandexAPI(getYandex());

	public static MongoDatabase getDatabase() {
		MongoClientURI uri = new MongoClientURI(getMongoDB());
		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient.getDatabase("bumblecore");
	}
	
	private static String getYT() {
		return getKeys().getString("youtube");
	}
	
	private static String getMusixMatch() {
		return getKeys().getString("musicxmatch");
	}
	
	private static String getOSU() {
		return getKeys().getString("osu");
	}
	
	private static String getTMDB() {
		return getKeys().getString("tmdb");
	}
	
	private static String getTrakt() {
		return getKeys().getString("trakt");
	}
	
	private static String getYandex() {
		return getKeys().getString("yandex");
	}
	
	private static String getUrban() {
		return getKeys().getString("urban");
	}
	
	private static JSONObject getKeys() {		
		return config.getJSONObject("apiKeys");
	}

	private static String getMongoDB() {
		return config.getValue("database");
	}

	public static String getToken() {
		return config.getValue("token");
	}
	
	public static String getPrefix() {
		return config.getValue("prefix");
	}
	
	public static void setPrefix(String newPrefix) {
		config.setValue("prefix", newPrefix);
	}
	
	public static String getHelpWord() {
		return config.getValue("helpWord");
	}

	public static void setHelpWord(String newHelpWord) {
		config.setValue("helpWord", newHelpWord);
	}

	public static String getOwnerId() {
		return config.getValue("ownerId");
	}
	
	public static Integer getServerId() {
		return getServer().getInt("id");
	}
	
	public static Integer getCleverTC() {
		return getServer().getInt("chatTextChannelId");
	}
	
	public static Integer getServerTC() {
		return getServer().getInt("joinTextChannelID");
	}
	
	private static JSONObject getServer() {
		return config.getJSONObject("server");
	}

	public static String getHex() {
		return config.getValue("hex");
	}

	public static void setHex(String newHex) {
		config.setValue("hex", newHex);
	}
	
	public static String[] getAdmins() {
		return OtherUtil.toStringArray(config.getJSONArray("developersId"));
	}

	public static String getWebhookURL() {
		return config.getValue("webhook");
	}

}
