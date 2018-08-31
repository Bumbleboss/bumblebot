package commands.api.osu;

import java.awt.Color;
import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import osuAPI.OsuAPI;
import osuAPI.OsuBeatmap;
import osuAPI.OsuPlay;
import utility.ConfigUtil;
public class OsuBestCmd extends Command {

	public OsuBestCmd() {
		this.name = "osubest";
		this.help = "Gets top plays of an osu! player.";
		this.aliases = new String[] {"osbt"};
		this.category = Bumblebot.API;
		this.arguments = "(username) {} Bumbleboss";
	}
	
	@Override
	@SuppressWarnings("static-access")
	protected void execute(CommandEvent e) {
		OsuAPI api = ConfigUtil.osu;
		String user = e.getArgs().isEmpty() ? e.getAuthor().getName() : e.getArgs();

		try {
			ArrayList<OsuPlay> beatmaps = api.getBestPlays(user, 0);
			EmbedBuilder eb = new EmbedBuilder();
				
			eb.setAuthor("Top plays of " + user, "https://osu.ppy.sh/u/"+beatmaps.get(0).getUserId(), "https://a.ppy.sh/"+beatmaps.get(0).getUserId());
			eb.setColor(Color.decode(ConfigUtil.getHex()));
			for(int i = 0; i < 3 && i < beatmaps.size(); i++) {
				OsuPlay beat = beatmaps.get(i);
				OsuBeatmap ub = api.getBeatmapsById(beatmaps.get(i).getBeatmapId(), 0).get(0);
				String beatname = ub.getArtist() + " - " + ub.getTitle() + " [" + ub.getDifficultyName() + "]";
				
				StringBuilder star = new StringBuilder();
				for(int i1 = 0; i1  < Math.round(Double.parseDouble(ub.getDifficultyRating())); i1++) {
					star.append("⭐");
				}
				eb.addField("Top Rank #"+ (i+1), "["+beatname+"](https://osu.ppy.sh/b/"+ub.getBeatmapId()+")\n"+"Combo: " +beat.getMaxCombo()
				+" | Misses: " + beat.getCountMiss() +" | Rank: " + beat.getRank() +" | Difficulty: `"+star+"`", false);
			}
			e.reply(eb.build());
		}catch(IndexOutOfBoundsException ex) {
			e.reply("No users were found! ;-;");
		}
	}
}
