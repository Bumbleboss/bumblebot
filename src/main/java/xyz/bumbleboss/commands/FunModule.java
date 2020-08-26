package xyz.bumbleboss.commands;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.jockie.bot.core.argument.Argument;
import com.jockie.bot.core.argument.Endless;
import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;

import org.json.simple.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import org.apache.commons.math3.util.Pair;

import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;

@Module
public class FunModule {

  @Command(value="choose", description="Makes a decision with the provided choices", aliases="random")
	public void choose(CommandEvent e, @Endless @Argument("choices") String[] choices) {
    
    List<Pair<String, Double>> replies = Util.ArrayToPairList(new String[] {
      "Lovin'", "I would pick", "I think I would go with", ":point_right:"
    });

    Util.respond(e, String.format("%s **%s**",
      Util.getRandom(replies), Util.getRandom(Util.ArrayToPairList(choices))
    ));
  }

  @Command(value = "birthday", description = "Wishes you a happy birthday!", aliases = "bd")
  public void birthday(CommandEvent e, @Argument("User") User usr) {
    Util.respond(e, new EmbedBuilder()
      .setDescription("HAPPY BIRTHDAY " + usr.getName().toUpperCase())
      .setColor(Color.decode(Constants.COLOR))
      .setImage("https://media.tenor.com/images/d48f0a731ae8274f5752f059a5af8bfd/tenor.gif")
      .setTimestamp(Instant.now()).build()
    );
    Util.respond(e, "🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂");
  }

  @Command(value = "ship", description = "Shipping people has never been better!")
  public void ship(CommandEvent e, @Argument("User") Optional<User> usr, @Argument("User") User usr2) {
    User usr1 = usr.orElse(e.getAuthor());
    
    Util.respond(e, new EmbedBuilder()
      .setDescription(shipMsg())
      .setColor(Color.decode(Constants.COLOR))
      .setFooter(usr1.getName() + " ❤ " + usr2.getName())
      .build()
    );
  }

  private String shipMsg() {
    Random r = new Random();
    int Low = 1;
    int High = 100;
    int result = r.nextInt(High - Low);
    StringBuilder sb = new StringBuilder();

    if (result >= 1 && result <= 10) {
      sb.append("**").append(result).append("%** `​█         ​` It's not gonna work ;-;");
    } else if (result >= 11 && result <= 20) {
      sb.append("**").append(result).append("%** `​██        ​` Forget it...");
    } else if (result >= 21 && result <= 30) {
      sb.append("**").append(result).append("%** `​█​██       ​` Very bad.");
    } else if (result >= 31 && result <= 40) {
      sb.append("**").append(result).append("%** `​█​███      ​` Not too good.");
    } else if (result >= 41 && result <= 50) {
      sb.append("**").append(result).append("%** `█​████     ​` Almost");
    } else if (result >= 51 && result <= 60) {
      sb.append("**").append(result).append("%** `█​█████    ​` Almost");
    } else if (result >= 61 && result <= 68) {
      sb.append("**").append(result).append("%** `​█​██████   ​` Not too shabby!");
    } else if (result == 69) {
      sb.append("**").append(result).append("%** `​█​██████   ​` Oh I see how it is ( ͡° ͜ʖ ͡°)");
    } else if (result >= 70 && result <= 80) {
      sb.append("**").append(result).append("%** `​█​███████  ​` Take good care!");
    } else if (result >= 81 && result <= 90) {
      sb.append("**").append(result).append("%** `​█​████████ ​` My my, I'm so envious.");
    } else if (result >= 91 && result <= 99) {
      sb.append("**").append(result).append("%** `​█​████████ ​` Damn :o");
    } else if (result == 100) {
      sb.append("**").append(result).append("%** `​█​█████████​` SUGOOOOOI!! YOU TWO ARE MEANT FOR EACH OTHER!!");
    } else {
      sb.append("**").append(result).append("%** `​          ​` Okay, it's pointless.");
    }

    return sb.toString();
  }

  @Command(value = "hug", description = "Hug someone >//<")
  public void hug(CommandEvent e, @Argument("User") Optional<User> usr) {
    User author = e.getAuthor();
    User user = usr.orElse(author);
    String[] data = nekoVal("hug", author == user, author, user);

    Util.respond(e, new EmbedBuilder()
      .setDescription(data[0])
      .setColor(Color.decode(Constants.COLOR))
      .setImage(data[1])
      .build()
    );
  }

  @Command(value = "kiss", description = "Kiss someone >o<")
  public void kiss(CommandEvent e, @Argument("User") Optional<User> usr) {
    User author = e.getAuthor();
    User user = usr.orElse(author);
    String[] data = nekoVal("kiss", author == user, author, user);

    Util.respond(e, new EmbedBuilder()
      .setDescription(data[0])
      .setColor(Color.decode(Constants.COLOR))
      .setImage(data[1])
      .build()
    );
  }

  @Command(value = "pat", description = "pat someone >.<")
  public void pat(CommandEvent e, @Argument("User") Optional<User> usr) {
    User author = e.getAuthor();
    User user = usr.orElse(author);
    String[] data = nekoVal("pat", author == user, author, user);

    Util.respond(e, new EmbedBuilder()
      .setDescription(data[0])
      .setColor(Color.decode(Constants.COLOR))
      .setImage(data[1])
      .build()
    );
  }

  private String[] nekoVal(String type, Boolean isAuthor, User author, User mentioned) {
    String msg = null;
    String img = nekoImg(type);

    List<List<Pair<String, Double>>> hugs = new ArrayList<List<Pair<String, Double>>>();
    List<List<Pair<String, Double>>> kisses = new ArrayList<List<Pair<String, Double>>>();
    List<List<Pair<String, Double>>> pats = new ArrayList<List<Pair<String, Double>>>();

    hugs.add(Util.ArrayToPairList(new String[] {
      "Why so lonely, here's a hug (づ｡◕‿‿◕｡)づ",
      "Why are you hugging yourself? ;-;", 
      "Have a hug from me instead!", 
      "Don't hug yourself ;-;" 
    }));
    hugs.add(Util.ArrayToPairList(new String[] { 
      "just gave a hug to", "hugged", "just sent a hug to" 
    }));

    kisses.add(Util.ArrayToPairList(new String[] { 
      "Why are you kissing yourself? ;-;",
      "Have a kiss from me instead!", 
      "Don't kiss yourself ;-;" 
    }));
    kisses.add(Util.ArrayToPairList(new String[] { 
      "just kissed", "kissed", "gave a kiss to", "french kissed"
    }));

    pats.add(Util.ArrayToPairList(new String[] {
      "Why are you patting yourself? ;-;", 
      "Have a pat from me instead!", 
      "Don't pat yourself ;-;" 
    }));
    pats.add(Util.ArrayToPairList(new String[] { 
      "just patted", "patted", "pat pat"
    }));

    if (type == "hug") {
      msg = nekoMsg(hugs, isAuthor, author, mentioned);
    } else if (type == "kiss") {
      msg = nekoMsg(kisses, isAuthor, author, mentioned);
    } else if (type == "pat") {
      msg = nekoMsg(pats, isAuthor, author, mentioned);
    }

    return new String[] {msg, img};
  }

  private String nekoImg(String type) {
    JSONObject img = (JSONObject) Util.getJSON(Util.GET("https://nekos.life/api/"+type));
    return (String) img.get("url");
  }

  private String nekoMsg(List<List<Pair<String, Double>>> responses, Boolean isAuthor, User author, User mentioned) {
    if (isAuthor) {
      return Util.getRandom(responses.get(1)) + " " + author.getAsMention();
    }
    
    return author.getAsMention() + " " + Util.getRandom(responses.get(0)) + " " + mentioned.getAsMention();
  }
}