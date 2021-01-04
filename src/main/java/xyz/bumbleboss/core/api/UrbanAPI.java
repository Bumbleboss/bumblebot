package xyz.bumbleboss.core.api;

import org.json.JSONObject;


import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;

public class UrbanAPI {

  private String word;
  
  public UrbanAPI(String word) {
    this.word = word;
  }

  private JSONObject data = (JSONObject) Util.getJSON(Util.GET(
    "https://mashape-community-urban-dictionary.p.rapidapi.com/define?term="+getWord(),
    new String[][] {
      {"x-rapidapi-key", Constants.URBAN_KEY}, 
      {"x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com"}
    }
  ));

  private String getWord() {
    return word;
  }

  public String working() {
    return data.getJSONArray("list").getJSONObject(0).getString("word").toString();
  } 
}