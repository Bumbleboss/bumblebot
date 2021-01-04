package xyz.bumbleboss.commands;

import java.awt.Color;
import java.time.Instant;
import java.util.*;

import com.jockie.bot.core.argument.Argument;
import com.jockie.bot.core.argument.Endless;
import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;

import org.apache.commons.math3.util.Pair;

import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.api.NekoAPI;
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
  public void birthday(CommandEvent e, @Argument("User") User user) {
    Util.respond(e, new MessageBuilder().setEmbed(
        new EmbedBuilder()
        .setDescription("HAPPY BIRTHDAY " + user.getName().toUpperCase())
        .setColor(Color.decode(Constants.COLOR))
        .setImage("https://media.tenor.com/images/d48f0a731ae8274f5752f059a5af8bfd/tenor.gif")
        .setTimestamp(Instant.now())
        .build()
      )
      .setContent("🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂🎉🎂")
    );
  }

  @Command(value = "ship", description = "Shipping people has never been better!")
  public void ship(CommandEvent e, @Argument("User") User user, @Argument("User") Optional<User> user2) {
    EmbedBuilder eb = new EmbedBuilder();

    eb.setDescription(shipMsg());
    eb.setColor(Color.decode(Constants.COLOR));

    if (user2.isPresent()) {
      eb.setFooter(user.getName() + " ❤ " + user2.get().getName());
    } else {
      eb.setFooter(e.getAuthor().getName() + " ❤ " + user.getName());
    }
    
    Util.respond(e, eb.build());
  }

  @SuppressWarnings("SpellCheckingInspection")
  private String shipMsg() {
    Random r = new Random();
    int High = 101;
    int result = r.nextInt(High);
    StringBuilder sb = new StringBuilder();

    if (result >= 1 && result <= 10) {
      sb.append("**").append(result).append("%** `​█         ​` It's not gonna work ;-;");
    } else if (result >= 11 && result <= 20) {
      sb.append("**").append(result).append("%** `​██        ​` Forget it...");
    } else if (result >= 21 && result <= 30) {
      sb.append("**").append(result).append("%** `​███       ​` Very bad.");
    } else if (result >= 31 && result <= 40) {
      sb.append("**").append(result).append("%** `​████      ​` Not too good.");
    } else if (result >= 41 && result <= 50) {
      sb.append("**").append(result).append("%** `​█████     ​` Almost");
    } else if (result >= 51 && result <= 60) {
      sb.append("**").append(result).append("%** `​██████    ​` Almost");
    } else if (result >= 61 && result <= 68) {
      sb.append("**").append(result).append("%** `​███████   ​` Not too shabby!");
    } else if (result == 69) {
      sb.append("**").append(result).append("%** `​███████   ​` Oh I see how it is ( ͡° ͜ʖ ͡°)");
    } else if (result >= 70 && result <= 80) {
      sb.append("**").append(result).append("%** `​████████  ​` Take good care!");
    } else if (result >= 81 && result <= 90) {
      sb.append("**").append(result).append("%** `​█████████ ​` My my, I'm so envious.");
    } else if (result >= 91 && result <= 99) {
      sb.append("**").append(result).append("%** `​█████████ ​` Damn :o");
    } else if (result == 100) {
      sb.append("**").append(result).append("%** `​██████████​` SUGOOOOOI!! YOU TWO ARE MEANT FOR EACH OTHER!!");
    } else {
      sb.append("**").append(result).append("%** `​          ​` Okay, it's pointless.");
    }

    return sb.toString();
  }

  @Command(value = "hug", description = "Hug someone >//<")
  public void hug(CommandEvent e, @Argument("User") User user) {
    String[] data = NekoAPI.getData("hug", e.getAuthor(), user);

    Util.respond(e, new EmbedBuilder()
      .setDescription(data[0])
      .setColor(Color.decode(Constants.COLOR))
      .setImage(data[1])
      .build()
    );
  }

  @Command(value = "kiss", description = "Kiss someone >o<")
  public void kiss(CommandEvent e, @Argument("User") User user) {
    String[] data = NekoAPI.getData("kiss", e.getAuthor(), user);

    Util.respond(e, new EmbedBuilder()
      .setDescription(data[0])
      .setColor(Color.decode(Constants.COLOR))
      .setImage(data[1])
      .build()
    );
  }

  @Command(value = "pat", description = "pat someone >.<")
  public void pat(CommandEvent e, @Argument("User") User user) {
    String[] data = NekoAPI.getData("pat", e.getAuthor(), user);

    Util.respond(e, new EmbedBuilder()
      .setDescription(data[0])
      .setColor(Color.decode(Constants.COLOR))
      .setImage(data[1])
      .build()
    );
  }
}