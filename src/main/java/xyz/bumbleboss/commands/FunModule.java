package xyz.bumbleboss.commands;

import java.awt.Color;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import com.jockie.bot.core.argument.Argument;
import com.jockie.bot.core.argument.Endless;
import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;

import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;

@Module
public class FunModule {

  @Command(value="choose", description="Makes a decision with the provided choices", aliases="random")
	public void choose(CommandEvent e, @Endless @Argument("choices") String[] choices) {
    String[] replies = new String[] {"I choose", "I would pick", "I think I would go with", ":point_right:"};
    e.reply(String.format("%s **%s**", Util.random(replies), Util.random(choices))).queue();
  }

  @Command(value="birthday", description="Wishes you a happy birthday!", aliases="bd")
	public void birthday(CommandEvent e, @Argument("User") User usr) {
    e.reply(new EmbedBuilder()
      .setDescription("HAPPY BIRTHDAY " + usr.getName().toUpperCase())
		  .setColor(Color.decode(Constants.HEX))
			.setImage("https://media.tenor.com/images/d48f0a731ae8274f5752f059a5af8bfd/tenor.gif")
			.setTimestamp(Instant.now())
      .build())
    .queue();
		e.reply("🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂").queue();
  }

  @Command(value="ship", description="Shipping people has never been better!")
  public void ship(CommandEvent e, @Argument("User") Optional<User> usr, @Argument("User") User usr2) {
    User usr1 = usr.orElse(e.getAuthor()); 
    e.reply(new EmbedBuilder()
      .setDescription(shipMsg())
      .setColor(Color.decode(Constants.HEX))
      .setFooter(usr1.getName() + " ❤ " + usr2.getName())
      .build())
    .queue();
  }

  private String shipMsg() {
    Random r = new Random();
    int Low = 1;
    int High = 100;
    int result = r.nextInt(High-Low);
    StringBuilder sb = new StringBuilder();
    
    if(result >= 1 && result <= 10) {
      sb.append("**").append(result).append("%** `​█         ​` It's not gonna work ;-;");
    }else if(result >= 11 && result <= 20) {
      sb.append("**").append(result).append("%** `​██        ​` Forget it...");
    }else if(result >= 21 && result <= 30) {
      sb.append("**").append(result).append("%** `​█​██       ​` Very bad.");
    }else if(result >= 31 && result <= 40) {
      sb.append("**").append(result).append("%** `​█​███      ​` Not too good.");
    }else if(result >= 41 && result <= 50) {
      sb.append("**").append(result).append("%** `█​████     ​` Almost");
    }else if(result >= 51 && result <= 60) {
      sb.append("**").append(result).append("%** `█​█████    ​` Almost");
    }else if(result >= 61 && result <= 68) {
      sb.append("**").append(result).append("%** `​█​██████   ​` Not too shabby!");
    }else if(result == 69) {
      sb.append("**").append(result).append("%** `​█​██████   ​` Oh I see how it is ( ͡° ͜ʖ ͡°)");
    }else if(result >= 70 && result <= 80) {
      sb.append("**").append(result).append("%** `​█​███████  ​` Take good care!");
    }else if(result >= 81 && result <= 90) {
      sb.append("**").append(result).append("%** `​█​████████ ​` My my, I'm so envious.");
    }else if(result >= 91 && result <= 99) {
      sb.append("**").append(result).append("%** `​█​████████ ​` Damn :o");
    }else if(result == 100) {
      sb.append("**").append(result).append("%** `​█​█████████​` SUGOOOOOI!! YOU TWO ARE MEANT FOR EACH OTHER!!");
		}else{
			sb.append("**").append(result).append("%** `​          ​` Okay, it's pointless.");
    }
    return sb.toString();
	}

  @Command(value="hug", description="Hug someone >//<")
	public void hug(CommandEvent e, @Argument("User") Optional<User> usr) {
    User author = e.getAuthor();
    User user = usr.orElse(author);
    
    String[] data = nekoVal("hug", author == user, author, user);

    e.reply(new EmbedBuilder()
      .setDescription(data[0])
		  .setColor(Color.decode(Constants.HEX))
			.setImage(data[1])
      .build())
    .queue();
  }

  @Command(value="kiss", description="Kiss someone >o<")
	public void kiss(CommandEvent e, @Argument("User") Optional<User> usr) {
    User author = e.getAuthor();
    User user = usr.orElse(author);
    
    String[] data = nekoVal("kiss", author == user, author, user);

    e.reply(new EmbedBuilder()
      .setDescription(data[0])
		  .setColor(Color.decode(Constants.HEX))
			.setImage(data[1])
      .build())
    .queue();
  }

  @Command(value="pat", description="pat someone >.<")
	public void pat(CommandEvent e, @Argument("User") Optional<User> usr) {
    User author = e.getAuthor();
    User user = usr.orElse(author);
    
    String[] data = nekoVal("pat", author == user, author, user);

    e.reply(new EmbedBuilder()
      .setDescription(data[0])
		  .setColor(Color.decode(Constants.HEX))
			.setImage(data[1])
      .build())
    .queue();
  }

  private String[] nekoVal(String type, Boolean isAuthor, User author, User mentioned) {
    String msg = null;
    String img = nekoImg(type);
    
    String[] h = new String[] {"Why so lonely, here's a hug (づ｡◕‿‿◕｡)づ", "Why are you hugging yourself? ;-;", "Have a hug from me instead!", "Don't hug yourself ;-;"};
    String[] h2 = new String[] {"just gave a hug to", "hugged", "just sent a hug to"};

		String[] k = new String[] {"Why are you kissing yourself? ;-;", "Have a kiss from me instead!", "Don't kiss yourself ;-;"};		
		String[] k2 = new String[] {"just kissed", "kissed", "gave a kiss to", "french kissed"};

    String[] p = new String[] {"Why are you patting yourself? ;-;", "Have a pat from me instead!", "Don't pat yourself ;-;"};		
    String[] p2 = new String[] {"just patted", "patted", "pat pat"};
    
    if(type == "hug") {
      msg = nekoMsg(h, h2, isAuthor, author, mentioned);
    }else if(type == "kiss") {
      msg = nekoMsg(k, k2, isAuthor, author, mentioned);
    }else if(type == "pat") {
      msg = nekoMsg(p, p2, isAuthor, author, mentioned);
    }

    return new String[] {msg, img};
  }

  private String nekoImg(String type) {
    JSONObject img = (JSONObject) Util.getJSON(Util.GET("https://nekos.life/api/"+type));
    return img.getString("url");
  }

  private String nekoMsg(String[] msgs, String[] msgs2, Boolean isAuthor, User author, User mentioned) {
    String msg = null;
    if(isAuthor) {
      msg = Util.random(msgs) + " " + author.getAsMention();
    }else{
      msg = author.getAsMention() + " " + Util.random(msgs2) + " " + mentioned.getAsMention();
    }
    return msg;
  }
}