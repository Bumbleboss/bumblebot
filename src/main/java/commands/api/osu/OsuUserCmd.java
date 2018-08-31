package commands.api.osu;

import java.awt.Color;
import java.text.DecimalFormat;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;

import net.dv8tion.jda.core.EmbedBuilder;
import osuAPI.OsuAPI;
import osuAPI.OsuBeatmap;
import osuAPI.OsuUser;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;

public class OsuUserCmd extends Command {

	public OsuUserCmd() {
		this.name = "osu";
		this.aliases = new String[] {"osur"};
		this.help = "Gets data of an osu! player.";
		this.arguments = "(username) {} Bumbleboss";
		this.category = Bumblebot.API;
	}

	@Override
	@SuppressWarnings("static-access")
	protected void execute(CommandEvent e) {
		OsuAPI api = ConfigUtil.osu;
		String user = e.getArgs().isEmpty() ? e.getAuthor().getName() : e.getArgs();

		try{
			OsuUser usr = api.getUser(user, 0);
			EmbedBuilder eb = new EmbedBuilder();
			DecimalFormat df = new DecimalFormat("####0");
			DecimalFormat dfa = new DecimalFormat("####0.00");
				
			
			eb.setAuthor(usr.getUsername(), "https://osu.ppy.sh/u/"+usr.getUserId(), "https://a.ppy.sh/"+usr.getUserId());
			eb.setColor(Color.decode(ConfigUtil.getHex()));
			eb.addField("Rank","#"+ df.format(Double.parseDouble(usr.getPPRank())), true);
			eb.addField("Country", (user.equalsIgnoreCase("Bumbleboss")?":eyes:":":flag_"+usr.getCountry().toLowerCase()+":"), true);
			eb.addField("PP", df.format(Double.parseDouble(usr.getPPRaw()))+"pp", true);
			eb.addField("Accuracy", dfa.format(Double.parseDouble(usr.getAccuracy()))+"%", true);
			eb.addField("Total Plays", usr.getPlayCount(), true);
			eb.addField("Level", df.format(Double.parseDouble(usr.getLevel())), true);
				
			OsuBeatmap ub = api.getBeatmapsById(api.getBestPlays(user, 0).get(0).getBeatmapId(), 0).get(0);
			String Beatmap = ub.getArtist() + " - " + ub.getTitle() + " [" + ub.getDifficultyName() + "]";
			eb.addField("Top Play", "["+Beatmap+"](https://osu.ppy.sh/b/"+ub.getBeatmapId()+")", false);
			eb.setFooter("Requested by "+UsrMsgUtil.getUserSet(e.getJDA(), e.getAuthor().getId()), e.getAuthor().getAvatarUrl());
			e.reply(eb.build());
		}catch (Exception ex) {
			if(ex instanceof NullPointerException) {
				e.reply("No users were found! ;-;");
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}