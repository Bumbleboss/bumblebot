package xyz.bumbleboss.commands;

import java.awt.Color;
import java.util.*;

import com.jockie.bot.core.argument.Argument;
import com.jockie.bot.core.command.Command;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.module.Module;
import com.jockie.bot.core.utility.ArgumentUtility;

import org.json.simple.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.core.Util;

@Module
public class InfoModule {
  
  @Command(value="serverinfo", description="Provides detailed information about the server", aliases={"si"})
	public void serverInfo(CommandEvent e) {
    EmbedBuilder eb = new EmbedBuilder();
    Guild guild = e.getGuild();
    
    String info = String.format("Owner: **%s**\nRegion: **%s** %s\nVerification Level: **%s**\nID: **%s**",
      Objects.requireNonNull(guild.getOwner()).getUser().getAsTag(),
      guild.getRegion().getName(), 
      guild.getRegion().getEmoji(),
      guild.getVerificationLevel().name(),
      guild.getId()
    );

    String channels = String.format("Categories: **%s**\nText Channels: **%s**\nVoice Channels: **%s**", 
      guild.getCategories().size(), 
      guild.getTextChannels().size(), 
      guild.getVoiceChannels().size()
    );

    int[] mems = Util.getMembersType(guild);
    String users = String.format("All: **%s**\nBots: **%s**\nUsers: **%s**",
      mems[6], mems[4], mems[5]
    );

    String footer = String.format("Created on %s", 
      String.format("%1$tb %1$te, %1$tY", guild.getTimeCreated())
    );
    
    eb.setAuthor(guild.getName(), null, guild.getIconUrl());
    eb.setThumbnail(guild.getIconUrl()); 
    eb.addField("Info", info, true);
    eb.addBlankField(true);
    eb.addBlankField(true);
    eb.addField("Channels", channels, true);
    eb.addField("Members", users, true);
    eb.setFooter(footer);
    eb.setColor(Color.decode(Constants.COLOR));
  
    Util.respond(e, eb.build());  
  }

  @Command(value="userinfo", description="Returns with information about a certain user", aliases={"ui"})
	public void userInfo(CommandEvent e, @Argument("user") Optional<String> user) {
    EmbedBuilder eb = new EmbedBuilder();

    ArgumentUtility.retrieveUserById(e.getJDA(), user.orElse(e.getAuthor().getId())).queue(ur -> {
      eb.setThumbnail(ur.getAvatarUrl());
      eb.addField("Username", ur.getAsTag(), false);
      eb.addField("ID", ur.getId(), true);
      eb.addBlankField(true);
      eb.addField("Created on", String.format("%1$tb %1$te, %1$tY", ur.getTimeCreated()), true);
      
      if (e.getChannelType().isGuild()) {
        if (e.getGuild().isMember(ur)) {
          Member mem = e.getGuild().getMember(ur);
          assert mem != null;
          List<Activity> act = mem.getActivities();
  
          eb.addField("Joined on", String.format("%1$tb %1$te, %1$tY", mem.getTimeJoined()), true);
          eb.addBlankField(true);
          eb.addField("Status", mem.getOnlineStatus().toString().replace("_", " "), true);
          eb.addField("Activity", (act.size() != 0 ? act.get(0).getName() : "N/A"), false);
        }
  
        eb.setFooter(String.format("Requested by %s", e.getAuthor().getAsTag()), e.getAuthor().getAvatarUrl());
      }
  
      eb.setColor(Color.decode(Constants.COLOR));
      Util.respond(e, eb.build());
    });
  }

  @Command(value="botinfo", description="Know more about me!", aliases={"bi"})
	public void botInfo(CommandEvent e) {
    EmbedBuilder eb = new EmbedBuilder();
    JDA jda = e.getJDA();
    User me = jda.getSelfUser();

    eb.setAuthor(me.getName(), null, me.getAvatarUrl());
    eb.setThumbnail(me.getAvatarUrl());

    eb.setDescription(String.format(
      "I'm **%s**, a bot that is dedicated for [this server](https://discord.gg/7PCdKYN)\n"
      +"I will be your personal not-hentai waifu! I can cook, wash, str-- *cough* I will be your assistant!!"
      +" I can stalk user's info and many more useless **%s** commands! v**%s**"
      +"\n\nTo know what I can do, type **%s**"
      +"\nFeel free to support me on [Ko-fi](https://ko-fi.com/bumblecore)"
      +"\n\nNo idea what a command is about? Type **%s**!", 
      me.getName(), e.getCommandListener().getAllCommands().size(),
      "2", Constants.PREFIX+"help", Constants.PREFIX+"help <command>"
    ));

    eb.addField("OS", System.getProperty("os.name"), true);
    eb.addField("Library", "JDA "+ JDAInfo.VERSION, true);

    Activity gm = jda.getPresence().getActivity();
    eb.addField("Activity", (gm != null ? gm.getName() : "N/A"), false);
    eb.addField("Uptime", Util.getUptime(), true);
	
    eb.setFooter(String.format("Developers: %s, %s | Host: %s", 
      Objects.requireNonNull(jda.getUserById(Constants.OWNER_ID)).getName(),
      Objects.requireNonNull(jda.getUserById(Constants.DEVS_ID[3])).getName(),
      Objects.requireNonNull(jda.getUserById(Constants.HOST_ID)).getName()
    ));

    eb.setColor(Color.decode(Constants.COLOR));
    Util.respond(e, eb.build());
  }

  @Command(value="avatar", description="Get an avatar of a user", aliases={"ava"})
	public void avatar(CommandEvent e, @Argument("user") Optional<String> user) {
    ArgumentUtility.retrieveUserById(e.getJDA(), user.orElse(e.getAuthor().getId())).queue(ur ->
      Util.respond(e, new EmbedBuilder()
        .setAuthor(ur.getName(), ur.getAvatarUrl())
        .setImage(ur.getAvatarUrl()+"?size=2048")
        .setFooter("Lookin' hot")
        .setColor(Color.decode(Constants.COLOR))
        .build()
      )
    );
  }

  @Command(value="uptime", description="Check for how long I have been running :sweat_drops:")
	public void uptime(CommandEvent e) {
    Util.respond(e, new EmbedBuilder().setDescription(Util.getUptime()).setColor(Color.decode(Constants.COLOR)).build());
  }

  @Command(value="version", description="Check the version of the bot and it's changelog", aliases={"ver", "changelogs", "changes"})
	public void version(CommandEvent e) {
    String json =  Util.GET("https://api.github.com/repos/Bumbleboss/bumblebot/releases");
    JSONArray js = (JSONArray) Util.getJSON(json);

    if (js.size() > 0) {
      JSONObject data = (JSONObject) js.get(0);
      EmbedBuilder eb = new EmbedBuilder();

      eb.addField("Version", data.get("tag_name").toString(), false);
      eb.addField("Description", data.get("body").toString(), false);
      eb.setFooter(String.format("On %1$tb %1$te, %1$tY",
        Util.getDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", data.get("published_at").toString())
      ));
      eb.setColor(Color.decode(Constants.COLOR));
  
      Util.respond(e, eb.build());
    } else {
      Util.respond(e, "There are no public releases at the moment...");
    }
  }
}