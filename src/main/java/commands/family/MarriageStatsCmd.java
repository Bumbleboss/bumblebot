package commands.family;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

import java.awt.*;

public class MarriageStatsCmd extends Command {

    public MarriageStatsCmd() {
        this.name = "mstats";
        this.help = "Some statistics on the marriages and children";
        this.category = Bumblebot.Marriage;
    }

    @Override
    protected void execute(CommandEvent e) {
        Marriage mrg = new Marriage();
        Children chl = new Children();
        e.reply(new EmbedBuilder()
                .setColor(Color.decode(ConfigUtil.getHex()))
                .setTitle("Current stats")
                .setDescription("Marriages: **" + (mrg.getMarriages().size()/2)+"**\nChildren: **" + chl.getChildren().size()+"**")
                .build());
    }
}
