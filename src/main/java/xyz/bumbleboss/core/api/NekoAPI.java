package xyz.bumbleboss.core.api;

import net.dv8tion.jda.api.entities.User;
import xyz.bumbleboss.core.Util;

import org.apache.commons.math3.util.Pair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NekoAPI {

  public String message;
  public String image;

  public NekoAPI(String type, User user, User user2) {
    this.message = getMessage(messageType(type), user, user2);
    this.image = getImage(type);
  }

  private static String getImage(String type) {
    JSONObject img = (JSONObject) Util.getJSON(Util.GET("https://nekos.life/api/" + type));
    return img.getString("url");
  }

  private static String getMessage(List<List<Pair<String, Double>>> responses, User user, User user2) {
    if (user == user2) {
      return Util.getRandom(responses.get(0)) + " " + user.getAsMention();
    }
    return user.getAsMention() + " " + Util.getRandom(responses.get(1)) + " " + user2.getAsMention();
  }

  private static List<List<Pair<String, Double>>> messageType(String type) {
    List<List<Pair<String, Double>>> hugs = new ArrayList<>();
    List<List<Pair<String, Double>>> kisses = new ArrayList<>();
    List<List<Pair<String, Double>>> pats = new ArrayList<>();

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
      return hugs;
    } else if (Objects.equals(type, "kiss")) {
      return kisses;
    } else if (Objects.equals(type, "pat")) {
      return pats;
    }

    return null;
  }
}
