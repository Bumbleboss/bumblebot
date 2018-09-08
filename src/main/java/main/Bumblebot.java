package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.Command.Category;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import commands.api.ChatterBot;
import commands.api.anilist.AnimeCmd;
import commands.api.anilist.CharacterCmd;
import commands.api.osu.OsuBestCmd;
import commands.api.osu.OsuUserCmd;
import commands.api.trakt.TraktHistoryCmd;
import commands.api.trakt.TraktUserCmd;
import commands.api.youtube.YTChnlCmd;
import commands.api.youtube.YTVidCmd;
import commands.family.child.AbandonCmd;
import commands.family.child.AcceptAdoptCmd;
import commands.family.child.AdoptCmd;
import commands.family.child.CancelAdoptCmd;
import commands.family.child.ChildrenCmd;
import commands.family.child.DeclineAdoptCmd;
import commands.family.marriage.AcceptCmd;
import commands.family.marriage.CancelCmd;
import commands.family.marriage.DeclineCmd;
import commands.family.marriage.DivorceCmd;
import commands.family.marriage.ForceDivorceCmd;
import commands.family.marriage.ForceMarryCmd;
import commands.family.marriage.MarriageInfoCmd;
import commands.family.marriage.ProposeCmd;
import commands.fun.BirthdayCmd;
import commands.fun.ChooseCmd;
import commands.fun.ShippingCmd;
import commands.fun.nekoLife.HugNekoCmd;
import commands.fun.nekoLife.KissNekoCmd;
import commands.fun.nekoLife.PatNekoCmd;
import commands.info.AvatarCmd;
import commands.info.BotInfoCmd;
import commands.info.BotVersionCmd;
import commands.info.ServerInfoCmd;
import commands.info.ServerMembersInfoCmd;
import commands.info.UptimeCmd;
import commands.info.UserInfoCmd;
import commands.misc.HexCmd;
import commands.misc.LyricsCmd;
import commands.misc.MovieSearchCmd;
import commands.misc.ServerInviteCmd;
import commands.misc.TVShowSearchCmd;
import commands.misc.TranslateCmd;
import commands.misc.UrbanCmd;
import commands.misc.WikipediaCmd;
import commands.mod.BanCmd;
import commands.mod.CleanCmd;
import commands.mod.KickCmd;
import commands.mod.MuteCmd;
import commands.mod.VoteCmd;
import commands.music.ClearCmd;
import commands.music.GetPlaylistCmd;
import commands.music.NowPlayingCmd;
import commands.music.PauseCmd;
import commands.music.PlayCmd;
import commands.music.PlaylistCmd;
import commands.music.PositionCmd;
import commands.music.QueueCmd;
import commands.music.RemoveCmd;
import commands.music.RepeatCmd;
import commands.music.SearchCmd;
import commands.music.ShuffleCmd;
import commands.music.SkipCmd;
import commands.music.SkipToCmd;
import commands.music.StopCmd;
import commands.music.TrackInfoCmd;
import commands.myServer.AntiLink;
import commands.myServer.ServerJoins;
import commands.myServer.roles.AddRolesCmd;
import commands.myServer.roles.RemoveRolesCmd;
import commands.myServer.roles.RolesCmd;
import commands.myServer.rules.RulesCmd;
import commands.owner.ConfigCmd;
import commands.owner.DownloadCmd;
import commands.owner.EvalCmd;
import commands.owner.FilesCmd;
import commands.owner.LoggerCmd;
import commands.owner.ShutdownCmd;
import commands.owner.SudoCmd;
import commands.owner.UpdateCmd;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.FileManager;
import utility.jdu.HelpUtil;

public class Bumblebot {

	public static JDA jda;
	public final static Category API = new Category("APIs");
	public final static Category Info = new Category("Info");
	public final static Category Fun = new Category("Fun");
	public final static Category Misc = new Category("Misc");
	public final static Category Music = new Category("Music");
	public final static Category Mod = new Category("Moderation");
	public final static Category Marriage = new Category("Marriage");
	public final static Category myServer = new Category("Server");
	public final static Category Owner = new Category("Owner");
	public final static Permission[] modPerms = new Permission[] {Permission.MESSAGE_MANAGE, Permission.KICK_MEMBERS, Permission.BAN_MEMBERS, Permission.MANAGE_ROLES};
	private static final ScheduledExecutorService threadpool = Executors.newSingleThreadScheduledExecutor();
	private static final EventWaiter waiter = new EventWaiter();
	public static final String botVersion = FileManager.readFiles("./build.gradle").get(8).replace("'", "").replace("version", "").replace(" ", "");
	public static final Logger logger = LoggerFactory.getLogger("Bumblebot");

	public static void main(String[] args) throws LoginException {
		HelpUtil util = new HelpUtil();
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("com.sedmelluq.discord.lavaplayer").setLevel(Level.OFF);
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("net.dv8tion.jda.core.requests.Requester").setLevel(Level.OFF);
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("net.dv8tion.jda.core.audio.AudioWebSocket").setLevel(Level.OFF);

		util.setHelpCommands(getCommands());
		jda = new JDABuilder(AccountType.BOT).setToken(ConfigUtil.getToken()).setStatus(OnlineStatus.DO_NOT_DISTURB)
				.setAudioSendFactory(new NativeAudioSendFactory())
				.setGame(Game.playing("booting up...")).addEventListener(waiter).addEventListener(util)
				.addEventListener(new ChatterBot()).addEventListener(new AntiLink()).addEventListener(new ServerJoins())
				.addEventListener(getCommandClient())
				.build();
		ConfigUtil.main(new String[0]);
		changeStatus();
	}

	public static CommandClient getCommandClient() {
		CommandClientBuilder client = new CommandClientBuilder();
		client.setOwnerId(ConfigUtil.getOwnerId());
		client.setPrefix(ConfigUtil.getPrefix());
		client.setHelpWord(ConfigUtil.getHelpWord());
		client.setCoOwnerIds(ConfigUtil.getAdmins());
		client.addCommands(getCommands());
		client.setHelpConsumer((hl) -> {
			if(hl.getMessage().getContentDisplay().equalsIgnoreCase(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord())) {
				EmbedBuilder eb = new EmbedBuilder();

				eb.setAuthor(hl.getSelfUser().getName(), null, hl.getSelfUser().getAvatarUrl());
				eb.setDescription("Here you will find all the commands I can do.\n" +
						"Type **"+ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" <command>** to know a detailed info about the command.\n\n**Following categories:**\n");

				List<Command> cmds = Arrays.asList(getCommands());

				cmds.stream().map(Command::getCategory).distinct().filter(category -> {
				    if(category.equals(myServer)) {
				    	if(hl.getChannelType().equals(ChannelType.PRIVATE)) {
					        return false;
				    	}else{
					        return hl.getGuild().getId().equals(ConfigUtil.getServerId());
				    	}
				    }
				    if(category.equals(Mod)){
				    	if(hl.getChannelType().equals(ChannelType.PRIVATE)) {
				    		return false;
				    	}else{
				    		return hl.getMember().hasPermission(modPerms);
				    	}
				    }
				    if(category.equals(Owner)) {
				    	return hl.isOwner();
				    }
				    return true;
				}).forEach(category -> {
					for (Command cmd : cmds) {
						if (cmd.isHidden() && !hl.isOwner()) {
							continue;
						}
						if (cmd.isOwnerCommand()) {
							hl.isOwner();
						}

					}
				    eb.appendDescription("***"+category.getName() + "*** |");
				});
				eb.appendDescription("\n\nType **"+ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" <category>** to know what commands goes under the categories");
				eb.setColor(Color.decode(ConfigUtil.getHex()));
				eb.addField("Chit-chat", "Want to talk with me? Just tag me and I will respond to you! >//<", false);
				hl.reply(eb.build());
			}
		});
		return client.build();
	}

	public static Command[] getCommands() {
		return new Command[] {
				//INFO
				new ServerInfoCmd(), new UserInfoCmd(), new BotInfoCmd(), new ServerMembersInfoCmd(), new AvatarCmd(), new UptimeCmd(), new BotVersionCmd(),
				//APIS
				new AnimeCmd(waiter), new CharacterCmd(waiter), new OsuUserCmd(), new OsuBestCmd(),
				new TraktUserCmd(), new TraktHistoryCmd(waiter), new YTVidCmd(), new YTChnlCmd(),
				//FUN
				new ChooseCmd(), new BirthdayCmd(), new ShippingCmd(), new HugNekoCmd(), new KissNekoCmd(), new PatNekoCmd(),
				//MISC
				new ServerInviteCmd(), new LyricsCmd(), new UrbanCmd(), new MovieSearchCmd(), new TVShowSearchCmd(),
				new HexCmd(), new TranslateCmd(), new WikipediaCmd(),
				//MOD
				new CleanCmd(threadpool), new KickCmd(), new BanCmd(), new MuteCmd(), new VoteCmd(),
				//MARRIAGE
				new AcceptCmd(), new CancelCmd(), new DeclineCmd(), new DivorceCmd(), new ProposeCmd(), new MarriageInfoCmd(), new ForceMarryCmd(), new ForceDivorceCmd(),
				new AdoptCmd(), new AcceptAdoptCmd(), new AbandonCmd(), new DeclineAdoptCmd(), new ChildrenCmd(), new CancelAdoptCmd(),
				//MYSERVER
				new AddRolesCmd(), new RemoveRolesCmd(), new RolesCmd(), new RulesCmd(),
				//MUSIC
				new PlayCmd(), new PauseCmd(), new SkipCmd(), new ClearCmd(), new QueueCmd(waiter), new NowPlayingCmd(), new SearchCmd(waiter), new SkipToCmd(),
				new StopCmd(), new ShuffleCmd(), new RemoveCmd(), new RepeatCmd(), new PositionCmd(), new GetPlaylistCmd(), new PlaylistCmd(),
				new TrackInfoCmd(),
				//OWNER
				new EvalCmd(), new ConfigCmd(), new ShutdownCmd(), new FilesCmd(), new DownloadCmd(), new UpdateCmd(), new SudoCmd(), new LoggerCmd()
		};
	}

	private static void changeStatus() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				String fileName = "./assists/status.txt";
				BufferedReader input = null;

				ArrayList<String> lines = new ArrayList<>();
				File f = new File(fileName);

				try {
					input = new BufferedReader(new FileReader(f.getAbsolutePath()));
					String line;
					while((line = input.readLine()) != null) {
						lines.add(line);
					}
				}catch(IOException ex) {
					OtherUtil.getWebhookError(ex, this.getClass().getName(), null);
				}finally{
					if(input != null)
						try {
							input.close();
						} catch (IOException ex) {
							OtherUtil.getWebhookError(ex, this.getClass().getName(), null);
						}
				}
				jda.getPresence().setGame(Game.playing(ConfigUtil.getPrefix()+ConfigUtil.getHelpWord()+" | " + lines.get(new Random().nextInt(lines.size()))));
			}
		}, 0 , 150000);
	}
}
