package xyz.bumbleboss.core;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.json.simple.*;
import org.json.simple.parser.*;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bumbleboss.exceptions.validateFailedException;

import java.util.*;
import java.util.function.Function;

import com.jockie.bot.core.command.impl.CommandEvent;

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
  public static <T> T[] JSONArrayTo(JSONArray jsonArray, Class<T> type, Function<Object, T> conversionFunction) {
    T[] array = (T[]) Array.newInstance(type, jsonArray.size());
      
    for (int i = 0; i < jsonArray.size(); i++) {
      array[i] = conversionFunction.apply(jsonArray.get(i));
    }
      
    return array;
  }

  public static Date getDateFormat(String format, String date) {
    Date new_date = null;

    try {
      new_date = new SimpleDateFormat(format).parse(date); 
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    
    return new_date;
  }
  
  public static String getUptime() {
    Duration d = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
    StringBuilder sb = new StringBuilder();

    long dy = d.toDays();
    long hr = d.toHours() % 24;
    long min = d.toMinutes() % 60;
    long sec = d.getSeconds() % 60;

    if (dy != 0) {
      sb.append(dy).append(" days ");
    }

    if (hr != 0) {
      sb.append(hr).append(" hours ");
    }

    if (min != 0) {
      sb.append(min).append(" minutes ");
    }

    if (sec != 0) {
      sb.append(sec).append(" seconds");
    }

    return sb.toString();
  }

  public static String getRandom(List<Pair<String, Double>> items) {
    return new EnumeratedDistribution<>(items).sample();
  }

  public static <T> List<Pair<T, Double>> ArrayToPairList(T[] items) {
    List<Pair<T, Double>> list = new ArrayList<>();

    for (T item : items) {
      list.add(new Pair<>(item, 1.0));
    }
   
    return list;
  }
  
  // DISCORD
  public static int[] getMembersType(Guild guild) {
    int[] all = new int[7]; 

    List<Member> members = guild.getMembers();
    for (Member member : members) {
      if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
        all[0] += 1;
      } else if (member.getOnlineStatus() == OnlineStatus.IDLE) {
        all[1] += 1;
      } else if (member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
        all[2] += 1;
      } else if (member.getOnlineStatus() == OnlineStatus.OFFLINE) {
        all[3] += 1;
      }

      if (member.getUser().isBot()) {
        all[4] += 1;
      } else {
        all[5] += 1;
      }

      all[6] += 1;
    }

    return all;
  }

  public static void respond(CommandEvent e, MessageBuilder mb) {
    e.replyTyping().queue(q -> e.reply(mb.build()).queue());
  }

  public static void respond(CommandEvent e, String text) {
    e.replyTyping().queue(q -> e.reply(text).queue());
  }

  public static void respond(CommandEvent e, MessageEmbed embed) {
    e.replyTyping().queue(q -> e.reply(embed).queue());
  }
  
  // REST
  public static String GET(String url) {
    Request req = new Request.Builder().url(url).get().build();
		Response res;
    
    try {
			res = client.newCall(req).execute();
			return Objects.requireNonNull(res.body()).string();
		} catch (IOException ex) {
			ex.printStackTrace();
    }
    return null;
	}

  public static Object getJSON(String value) {
    JSONParser jsonParser = new JSONParser();
    Object obj = null;

    try {
      obj = jsonParser.parse(value);
    } catch (org.json.simple.parser.ParseException ex) {
      ex.printStackTrace();
    }

    return obj;
  }

  // HELPERS
  public static void validateJson(JSONObject jsonData, String path, String[] requiredKeys) throws validateFailedException {
    ArrayList<String> success = new ArrayList<>();
    ArrayList<String> failure = new ArrayList<>();
    StringBuilder output = new StringBuilder();

    for (String requiredKey : requiredKeys) {
      if (jsonData.containsKey(requiredKey)) {
        success.add(requiredKey);
      } else {
        failure.add(requiredKey);
      }
    }

    if (failure.size() > 0) {
      output.append("Found required keys: ");
      for (int i = 0; i < success.size(); i++) {
        output.append(success.get(i));
        if (i < success.size() - 1) {
          output.append(", ");
        }
      }

      output.append("\n Missing required keys: ");
      for (int i = 0; i < failure.size(); i++) {
        output.append(failure.get(i));
        if (i < failure.size() - 1) {
          output.append(", ");
        }
      }
      throw new validateFailedException(path, output.toString());
    }
  }
}