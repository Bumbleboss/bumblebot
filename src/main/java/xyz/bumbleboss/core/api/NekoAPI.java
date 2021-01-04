package xyz.bumbleboss.core.api;

import net.dv8tion.jda.api.entities.User;
import xyz.bumbleboss.core.Util;

import org.apache.commons.math3.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NekoAPI {

  public static String[] getData(String type, User author, User mentioned) {
    String msg = getMsg(messageType(type), author, mentioned);
    String img = getImg(type);
    return new String[]{msg, img};
  }

  private static String getImg(String type) {
    JSONObject img = (JSONObject) Util.getJSON(Util.GET("https://nekos.life/api/" + type));
    return (String) img.get("url");
  }

  private static String getMsg(List<List<Pair<String, Double>>> responses, User author, User mentioned) {
    if (author == mentioned) {
      return Util.getRandom(responses.get(0)) + " " + author.getAsMention();
    }
    return author.getAsMention() + " " + Util.getRandom(responses.get(1)) + " " + mentioned.getAsMention();
  }

  private static List<List<Pair<String, Double>>> messageType(String type) {
    List<List<Pair<String, Double>>> hugs = new ArrayList<>();
    List<List<Pair<String, Double>>> kisses = new ArrayList<>();
    List<List<Pair<String, Double>>> pats = new ArrayList<>();
    List<List<Pair<String, Double>>> messages = null;

    hugs.add(Util.ArrayToPairList(new String[]{
      "Why so lonely, here's a hug (づ｡◕‿‿◕｡)づ",
      "Why are you hugging yourself? ;-;",
      "Have a hug from me instead!",
      "Don't hug yourself ;-;"
    }));
    hugs.add(Util.ArrayToPairList(new String[]{
      "just gave a hug to", "hugged", "just sent a hug to"
    }));

    kisses.add(Util.ArrayToPairList(new String[]{
      "Why are you kissing yourself? ;-;",
      "Have a kiss from me instead!",
      "Don't kiss yourself ;-;"
    }));
    kisses.add(Util.ArrayToPairList(new String[]{
      "just kissed", "kissed", "gave a kiss to", "french kissed"
    }));
    
    pats.add(Util.ArrayToPairList(new String[]{
      "Why are you patting yourself? ;-;",
      "Have a pat from me instead!",
      "Don't pat yourself ;-;"
    }));
    pats.add(Util.ArrayToPairList(new String[]{
      "just patted", "patted", "pat pat"
    }));

    if (Objects.equals(type, "hug")) {
      messages = hugs;
    } else if (Objects.equals(type, "kiss")) {
      messages = kisses;
    } else if (Objects.equals(type, "pat")) {
      messages = pats;
    }

    return messages;
  }
}
