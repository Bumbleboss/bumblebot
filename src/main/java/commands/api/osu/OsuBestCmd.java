package commands.api.osu;

import java.awt.Color;
import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import main.Osu;
import main.OsuBeatmap;
import main.OsuPlay;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;

public class OsuBestCmd extends Command {

	public OsuBestCmd() {
		this.name = "osubest";
		this.help = "Gets top plays of an osu! player.";
		this.aliases = new String[] {"osbt"};
		this.category = Bumblebot.API;
		this.arguments = "<username> {} Bumbleboss";	
	}
	
	@Override
	@SuppressWarnings("static-access")
	protected void execute(CommandEvent e) {
		Osu api = ConfigUtil.osu;
		if(e.getArgs().isEmpty()) {
			e.reply("You need to provide an osu! username.");
			return;
		}
	
		try {
			ArrayList<OsuPlay> beatmaps = api.getBestPlays(e.getArgs(), 0);
			EmbedBuilder eb = new EmbedBuilder();
				
			eb.setAuthor("Top plays of " + e.getArgs(), "https://osu.ppy.sh/u/"+beatmaps.get(0).getUserId(), "https://a.ppy.sh/"+beatmaps.get(0).getUserId());
			eb.setColor(Color.decode(ConfigUtil.getHex()));
			for(int i = 0; i < 3 && i < beatmaps.size(); i++) {
				OsuPlay beat = beatmaps.get(i);
				OsuBeatmap ub = api.getBeatmapsById(beatmaps.get(i).getBeatmapId(), 0).get(0);
				String beatname = ub.getArtist() + " - " + ub.getTitle() + " [" + ub.getDifficultyName() + "]";
				
				String star = "";
				for(int i1 = 0; i1  < Math.round(Double.parseDouble(ub.getDifficultyRating())); i1++) {
					star += "⭐";
				}
				eb.addField("Top Rank #"+ (i+1), "["+beatname+"](https://osu.ppy.sh/b/"+ub.getBeatmapId()+")\n"+"Combo: " +beat.getMaxCombo()
				+" | Misses: " + beat.getCountMiss() +" | Rank: " + beat.getRank() +" | Difficulty: `"+star+"`", false);
			}
			e.reply(eb.build());
		}catch(IndexOutOfBoundsException ex) {
			if(ex instanceof IndexOutOfBoundsException) {
				e.reply("No users were found! ;-;");
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}
	}
}
