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
import musixMatchAPI.MusixMatch;
import osuAPI.OsuAPI;
import urbanAPI.UrbanAPI;
import yandexAPI.YandexAPI;
import youTubeAPI.YouTubeAPI;

public class ConfigUtil {

	private static final Config config = new Config();
	private static final AniListAPI aniapi = new AniListAPI();
	public static final OsuAPI osu = new OsuAPI(getOSU());
	public static final TraktV2 trakt = new TraktV2(getTrakt());
	public static final YouTubeAPI yt = new YouTubeAPI(getYT());
	
	public static final UrbanAPI ub = new UrbanAPI(getUrban());
	public static final MusixMatch lyrc = new MusixMatch(getMusixMatch());
	public static final Tmdb tmdb = new Tmdb(getTMDB());
	public static final YandexAPI trns = new YandexAPI(getYandex());

	@SuppressWarnings("static-access")
	public static void main (String[] args) {
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(() -> {
			try {
				aniapi.getAccessToken(getAniClient(), getAniSecret());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 0, 3720000, TimeUnit.MILLISECONDS);
	}

	public static MongoDatabase getDatabase() {
		MongoClientURI uri = new MongoClientURI(getMongoDB());
		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient.getDatabase("bumblecore");
	}
	
	private static String getYT() {
		return getKeys().getString("yt");
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
	
	private static String getAniSecret() {
		return getAnilist().getString("secret");
	}
	
	private static String getAniClient() {
		return getAnilist().getString("client");
	}
	
	private static JSONObject getAnilist() {
		return getKeys().getJSONObject("anilist");
	}
	
	private static JSONObject getKeys() {		
		return config.getJSONObject("keys");
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
		return OtherUtil.toStringArray(config.getJSONArray("admins"));
	}

	public static String getWebhookURL() {
		return config.getValue("web");
	}

	public static String getWebhookErrorURL() {
		return config.getValue("weberror");
	}


}
