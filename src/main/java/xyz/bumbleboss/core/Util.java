package xyz.bumbleboss.core;

import java.awt.Color;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.bumbleboss.bumblebot.Bot;
import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.exceptions.validateFailedException;

import java.util.*;
import java.util.function.Function;

import com.jockie.bot.core.command.ICommand;
import com.jockie.bot.core.command.impl.CommandEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class Util {

  private static final OkHttpClient client = new OkHttpClient();
  
  // JAVA
  @SuppressWarnings("unchecked")
  public static <T> T[] JSONArrayTo(JSONArray jsonArray, Class<T> type, Function<Object, T> conversionFunction) {
    T[] array = (T[]) Array.newInstance(type, jsonArray.length());
      
    for (int i = 0; i < jsonArray.length(); i++) {
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

  public static void respond(CommandEvent e, Boolean embed_only, String text) {
    respond(e, new EmbedBuilder().setDescription(text).setColor(Color.decode(Constants.COLOR)).build());
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

  public static String GET(String url, String[][] headers) {
    Headers.Builder hs = new Headers.Builder();
    for (int i = 0; i < headers.length; i++) {
      hs.add(headers[i][0], headers[i][1]);
    }
    
    Request req = new Request.Builder().url(url).get().headers(hs.build()).build();
		Response res;

    try {
			res = client.newCall(req).execute();
			return Objects.requireNonNull(res.body()).string();
		} catch (IOException ex) {
			ex.printStackTrace();
    }
    return null;
  }

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
  
  public static String POST(String url, String body) {
    RequestBody reqBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
    Request req = new Request.Builder().url(url).post(reqBody).build();
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
    JSONTokener json = new JSONTokener(value);
    return json.nextValue();
  }

  // HELPERS
  public static void validateJson(JSONObject jsonData, String path, String[] requiredKeys) throws validateFailedException {
    ArrayList<String> success = new ArrayList<>();
    ArrayList<String> failure = new ArrayList<>();
    StringBuilder output = new StringBuilder();

    for (String requiredKey : requiredKeys) {
      if (jsonData.has(requiredKey)) {
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

  public static String toHaste(String text) {
    String data = POST("https://hastebin.com/documents", text);

    if (data == null) {
      return "Hastebin is currently down";
    }    
    return String.format("https://hastebin.com/%s", new org.json.JSONObject(data).get("key").toString());
  }

  public static void postError(ICommand command, Throwable throwable) {
    StringWriter errors = new StringWriter();
		throwable.printStackTrace(new PrintWriter(errors));
    String link = String.format("[Error link](%s)", toHaste(errors.toString()));
    
    HashMap<String, Object> data = new HashMap<String, Object>();
    
    data.put("color", "#Cf0000");
    data.put("author_name", throwable.getClass().getName());
    data.put("author_icon", Bot.jda.getSelfUser().getAvatarUrl());
    data.put("text", String.format("%s\nCommand: %s", link, command.getCommand()));
    data.put("ts", Instant.now().getEpochSecond());

    org.json.JSONArray attachments = new org.json.JSONArray().put(0, new JSONObject(data));

    HashMap<String, Object> data2 = new HashMap<String, Object>();
    data2.put("attachments", attachments);

    String body = new JSONObject(data2).toString();
    POST(Constants.WEBHOOK+"/slack", body);
  }
}