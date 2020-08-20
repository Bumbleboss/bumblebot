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

import xyz.bumbleboss.bumblebot.Config;
import xyz.bumbleboss.bumblebot.Constants;
import xyz.bumbleboss.bumblebot.configValueMissingException;
import xyz.bumbleboss.core.Util;

@Module
public class InfoModule {
  
  @Command(value="serverinfo", description="Provides detailed information about the server", aliases={"si"})
	public void serverInfo(CommandEvent e) {
    EmbedBuilder eb = new EmbedBuilder();
    Guild guild = e.getGuild();
    
    String info = String.format("Owner: **%s**\nRegion: **%s** %s\nVerification Level: **%s**",
      Util.getFullName(guild.getOwner().getUser()),
      guild.getRegion().getName(), guild.getRegion().getEmoji(),
      Util.capatalize(guild.getVerificationLevel().name())
    );

    String channels = String.format("Categories: **%s**\nText Channels: **%s**\nVoice Channels: **%s**", 
      guild.getCategories().size(), guild.getTextChannels().size(), guild.getVoiceChannels().size()
    );

    int[] mems = Util.getMembers(guild);
    String users = String.format("All: **%s**\nBots: **%s**\nUsers: **%s**",
      mems[6], mems[4], mems[5]
    );

    eb.setAuthor(guild.getName(), null, guild.getIconUrl());
    eb.setThumbnail(guild.getIconUrl());
    
    eb.addField("Info", info, false);
    eb.addField("Channels", channels, true);
    eb.addField("Members", users, true);
    
    eb.setFooter(String.format("ID: %s | Created on ", guild.getId())).setTimestamp(guild.getTimeCreated());
    eb.setColor(Color.decode(Constants.HEX));
    e.reply(eb.build()).queue();
  }

  @Command(value="userinfo", description="Returns with information about a certain user", aliases={"ui"})
	public void userInfo(CommandEvent e, @Argument("user") Optional<String> usr) {
    EmbedBuilder eb = new EmbedBuilder();
    String user = usr.orElse(e.getAuthor().getId());

    ArgumentUtility.retrieveUser(e.getJDA(), user).queue(ur -> {
      eb.setThumbnail(ur.getAvatarUrl());
      eb.addField("Username", Util.getFullName(ur),true);
      eb.addField("ID", ur.getId(), true);
      eb.addField("Created on", String.format("%1$tb %1$te, %1$tY", ur.getTimeCreated()), true);
      
      if(e.getChannelType().isGuild()) {
        if(e.getGuild().isMember(ur)) {
          Member mem = e.getGuild().getMember(ur);
          List<Activity> act = mem.getActivities();
  
          eb.addField("Joined on", String.format("%1$tb %1$te, %1$tY", mem.getTimeJoined()), true);
          eb.addField("Status", Util.capatalize(mem.getOnlineStatus().toString()).replace("_", " "), true);
          eb.addField("Activity", (act.size() != 0 ? act.get(0).getName() : "N/A"), false);
        }
  
        eb.setFooter(String.format("Requested by %s", Util.getFullName(e.getAuthor())), e.getAuthor().getAvatarUrl());
      }
  
      eb.setColor(Color.decode(Constants.HEX));
      e.reply(eb.build()).queue();
    });
  }

  @Command(value="botinfo", description="Know more about me!", aliases={"bi"})
	public void botInfo(CommandEvent e) throws configValueMissingException {
    EmbedBuilder eb = new EmbedBuilder();
    JDA jda = e.getJDA();
    User me = jda.getSelfUser();

    eb.setAuthor(me.getName(), null, me.getAvatarUrl());
    eb.setThumbnail(me.getAvatarUrl());

    eb.setDescription(String.format(
      "Hello there! I'm **%s**. A bot that is dedicated for [this server](https://discord.gg/7PCdKYN)\n"
      +"I will be your personal waifu! I can cook, wash, str-- *cough* I mean... I will be your assistant!!"
      +" I can play music, check user's info and many more useless **%s** commands! v**%s**"
      +"\n\nTo know what functions I can do, type **%s**"
      +"\nFeel free to support me on [PayPal](https://www.paypal.me/bumbleboss)"
      +"\n\nNo idea what a command is about? Type **%s**!", 
      me.getName(), e.getCommandListener().getAllCommands().size(),
      "2", Constants.PREFIX+"help", Constants.PREFIX+"help <command>"
    ));

    eb.addField("OS", System.getProperty("os.name"), true);
    eb.addField("Library", "JDA "+ JDAInfo.VERSION, true);

    Activity gm = jda.getPresence().getActivity();
    eb.addField("Activity", (gm != null ? gm.getName() : "N/A"), false);
    eb.addField("Uptime", Util.getUptime(), true);
	
    eb.setFooter(String.format("Developed by %s | Hosted by %s", 
      Util.getFullName(jda.getUserById((String) Config.getConfigVal("ownerId"))),
      Util.getFullName(jda.getUserById((String) Config.getConfigVal("hostId")))
    ));

    eb.setColor(Color.decode(Constants.HEX));
    e.reply(eb.build()).queue();
  }

  @Command(value="avatar", description="Get an avatar of a user", aliases={"ava"})
	public void avatar(CommandEvent e, @Argument("user") Optional<String> usr) {
    String user = usr.orElse(e.getAuthor().getId());

    ArgumentUtility.retrieveUser(e.getJDA(), user).queue(ur -> {
      e.reply(new EmbedBuilder()
        .setAuthor(ur.getName(), ur.getAvatarUrl())
        .setImage(ur.getAvatarUrl()+"?size=2048")
        .setFooter("Lookin' hot")
        .setColor(Color.decode(Constants.HEX))
        .build()
      ).queue();
    });
  }

  @Command(value="uptime", description="Check for how long I have been running :sweat_drops:")
	public void uptime(CommandEvent e) {
    e.reply(new EmbedBuilder().setDescription(Util.getUptime()).setColor(Color.decode(Constants.HEX)).build()).queue();
  }

  @Command(value="version", description="Check the version of the bot and it's changelog", aliases={"ver", "changelogs", "changes"})
	public void version(CommandEvent e) {
    String json =  Util.GET("https://api.github.com/repos/Bumbleboss/bumblebot/commits?sha=2.0");
    JSONArray js = (JSONArray) Util.getJSON(json);
    
    JSONObject data = (JSONObject) js.get(0);
    JSONObject commit = (JSONObject) data.get("commit");
    JSONObject author = (JSONObject) commit.get("author");

    e.reply(new EmbedBuilder()
      .addField("Version", "2.0", false)
      .addField("Changelog", commit.get("message").toString(), false)
      .setFooter(String.format("On %1$tb %1$te, %1$tY", 
        Util.toDate("yyyy-MM-dd'T'HH:mm:ss'Z'", author.get("date").toString())
      ))
      .setColor(Color.decode(Constants.HEX))
      .build()
    ).queue();
  }
}