package utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.trakt5.TraktV2;

import aniListAPI.AniListAPI;
import main.Config;
import main.Osu;
import musixMatchAPI.MusixMatch;
import urbanAPI.UrbanAPI;
import yandexAPI.YandexAPI;
import youTubeAPI.YouTubeAPI;

public class ConfigUtil {

	private static Config config = new Config();
	public static AniListAPI aniapi = new AniListAPI();
	public static Osu osu = new Osu();
	public static TraktV2 trakt = new TraktV2(getTrakt());
	public static YouTubeAPI yt = new YouTubeAPI(getYT());
	
	public static UrbanAPI ub = new UrbanAPI(getUrban());	
	public static MusixMatch lyrc = new MusixMatch(getMusixMatch());
	public static Tmdb tmdb = new Tmdb(getTMDB());
	public static YandexAPI trns = new YandexAPI(getYandex());

	@SuppressWarnings("static-access")
	public static void main (String[] args) {
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					aniapi.getAccessToken(getAniClient(), getAniSecret());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 3720000, TimeUnit.MILLISECONDS);
		osu.setKey(getOSU());
	}

	public static MongoDatabase getDatabase() {
		MongoClientURI uri = new MongoClientURI(getMongoDB());
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("bumblecore");
		return database;
	}
	
	public static String getYT() {
		return getKeys().getString("yt");
	}
	
	public static String getMusixMatch() {
		return getKeys().getString("musicxmatch");
	}
	
	public static String getOSU() {
		return getKeys().getString("osu");
	}
	
	public static String getTMDB() {
		return getKeys().getString("tmdb");
	}
	
	public static String getTrakt() {
		return getKeys().getString("trakt");
	}
	
	public static String getYandex() {
		return getKeys().getString("yandex");
	}
	
	public static String getUrban() {
		return getKeys().getString("urban");
	}
	
	public static String getAniSecret() {
		return getAnilist().getString("secret");
	}
	
	public static String getAniClient() {
		return getAnilist().getString("client");
	}
	
	private static JSONObject getAnilist() {
		return getKeys().getJSONObject("anilist");
	}
	
	private static JSONObject getKeys() {		
		return config.getJSONObject("keys");
	}

	public static String getMongoDB() {
		return config.getValue("database");
	}

	public static final String getToken() {
		return config.getValue("token");
	}
	
	public static String getPrefix() {
		return config.getValue("prefix");
	}
	
	public static void setPrefix(String newPrefix) {
		config.setValue("prefix", newPrefix);
	}
	
	public static String getHelpWord() {
		return config.getValue("helpword");
	}

	public static void setHelpWord(String newHelpWord) {
		config.setValue("helpword", newHelpWord);
	}

	public static String getOwnerId() {
		return config.getValue("ownerid");
	}
	
	public static String getServerId() {
		return getServer().getString("id");
	}
	
	public static String getCleverTC() {
		return getServer().getString("clevertc");
	}
	
	public static String getServerTC() {
		return getServer().getString("servertc");
	}
	
	public static JSONObject getServer() {
		return config.getJSONObject("server");
	}

	public static String getHex() {
		return config.getValue("hex");
	}

	public static void setHex(String newHex) {
		config.setValue("hex", newHex);
	}
	
	public static String[] getAdmins() {
		return OtherUtil.toStringArray(config.getJSONArray("admins"));
	}

	public static String getWebhookURL() {
		return config.getValue("web");
	}

	public static String getWebhookErrorURL() {
		return config.getValue("weberror");
	}


}
