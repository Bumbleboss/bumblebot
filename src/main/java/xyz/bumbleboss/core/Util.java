package xyz.bumbleboss.core;

import org.json.simple.*;
import org.json.simple.parser.*;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bumbleboss.exceptions.dataValueMissingException;

import java.util.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class Util {

  private static final OkHttpClient client = new OkHttpClient();
  
  // JAVA
  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(JSONArray jsonArray, Class<T> type) {
      T[] array = (T[]) Array.newInstance(type, jsonArray.size());
      
      for(int i = 0; i < jsonArray.size()-1; i++) {
        array[i] = (T) jsonArray.get(i);
      }
      
      return array;
  }

  public static long[] toArrayLong(JSONArray jsonArray) {
    long[] array = {};

    try {
      for(int i = 0; i < jsonArray.size()-1; i++) {
        array[i] = (long) jsonArray.get(i);
      }
    }catch(ArrayIndexOutOfBoundsException ignored) {}

    return array;
  }

  public static Date toDate(String format, String date) {
    Date dt = null;
    try {
      dt = new SimpleDateFormat(format).parse(date); 
    } catch(ParseException ex) {
      ex.printStackTrace();
    }
    return dt;
  }
  
  public static String capatalize(String text) {
    return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
  }

  public static String getUptime() {
    Duration d = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
    StringBuilder sb = new StringBuilder();

    long dy = d.toDays();
    long hr = d.toHours() % 24;
    long min = d.toMinutes() % 60;
    long sec = d.getSeconds() % 60;

    if(dy != 0) {
      sb.append(dy + " days ");
    }

    if(hr != 0) {
      sb.append(hr + " hours ");
    }

    if(min != 0) {
      sb.append(min + " minutes ");
    }

    if(sec != 0) {
      sb.append(sec + " seconds");
    }
    return sb.toString();
  }

  public static String random(String... items) {
    return items[(int)(Math.random()*items.length)];
  }

  // DISCORD
  public static int[] getMembers(Guild gd) {
    int[] all = new int[7]; 

    List<Member> members = gd.getMembers();
    for(int i = 0; i < members.size(); i++) {
      Member mem = members.get(i);

      if(mem.getOnlineStatus() == OnlineStatus.ONLINE) {
        all[0] += 1;
      }else if(mem.getOnlineStatus() == OnlineStatus.IDLE) {
        all[1] += 1;
      }else if(mem.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
        all[2] += 1;
      }else if(mem.getOnlineStatus() == OnlineStatus.OFFLINE) {
        all[3] += 1;
      }

      if(mem.getUser().isBot()) {
        all[4] += 1;
      }else{
        all[5] += 1;
      }

      all[6] += 1;
    }
    return all;
  }

  public static String getFullName(User usr) {
    return usr.getName()+"#"+usr.getDiscriminator();
  }

  // REST
  public static String GET(String url) {
    Request request = new Request.Builder().url(url).get().build();
		Response response;
		try {
			response = client.newCall(request).execute();
			return Objects.requireNonNull(response.body()).string();
		} catch (IOException e) {
			return null;
		}
	}

  public static Object getJSON(String val) {
    JSONParser jsonParser = new JSONParser();
    Object js = null;

    try {
      js = jsonParser.parse(val);
    }catch(org.json.simple.parser.ParseException ex) {
      ex.printStackTrace();
    }
    return js;
  }

  public static Object validateJson(JSONObject jsonData, String path, String[] requiredKeys) throws dataValueMissingException {
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
      throw new dataValueMissingException(path, output.toString());
    }
    return true;
  }
}