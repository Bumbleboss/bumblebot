package xyz.bumbleboss.core.api;

import org.json.JSONException;
import org.json.JSONObject;


import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;

public class UrbanAPI {

  public JSONObject data;
  public String word;
  public String permalink;
  public String definition;
  public String example;
  public String author;
  public Integer thumbsUp;
  public Integer thumbsDown;

  public UrbanAPI(String wrd) {
    if (data == null) {
      data = requestData(wrd);
    } 
    if (data != null) {
      word = data.getString("word");
      permalink = data.getString("permalink");
      definition = formatText(data.getString("definition"), permalink);
      example = formatText(data.getString("example"), permalink);
      author = data.getString("author");
      thumbsUp = data.getInt("thumbs_up");
      thumbsDown = data.getInt("thumbs_down");
    }
  }

  private JSONObject requestData(String word) {
    JSONObject json = (JSONObject) Util.getJSON(Util.GET(
      String.format("https://mashape-community-urban-dictionary.p.rapidapi.com/define?term=%s", word),
      new String[][] {
        {"x-rapidapi-key", Constants.URBAN_KEY}, 
        {"x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com"}
      }
    ));

    try {
      return json.getJSONArray("list").getJSONObject(0);
    } catch (JSONException ex) {}   
    
    return null;
  }

  private String formatText(String text, String link) {
    text = text.replaceAll("\\[", "").replaceAll("\\]","");
    text = text.length() > 900 ? String.format("%s[...](%s)", text.substring(900), link): text;
    return text;
  }
}