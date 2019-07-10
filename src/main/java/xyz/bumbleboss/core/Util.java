package xyz.bumbleboss.core;

import org.json.simple.JSONArray;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;

import java.util.*;
import java.lang.reflect.Array;

public class Util {
  
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

  public static String capatalize(String text) {
    return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
  }

  public static String getFullName(User usr) {
    return usr.getName()+"#"+usr.getDiscriminator();
  }
}