package commands.info;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import utility.ConfigUtil;
import utility.OtherUtil;

import java.awt.*;
import java.util.List;

public class StatisticsCmd extends Command {

    public StatisticsCmd() {
        this.name = "stats";
        this.help = "Just commands statistics";
        this.aliases = new String[]{"stat", "statistics"};
        this.category = Bumblebot.Info;
    }

    @Override
    protected void execute(CommandEvent e) {
        List<Command> list = e.getClient().getCommands();
        StringBuilder sb = new StringBuilder();

        int max = 0;
        int scndMax = 0;

        String topCmd = null;
        String scndCmd = null;

        for (Command cmd : list) {
            int use = e.getClient().getCommandUses(cmd);
            if(use > max) {
                scndMax = max;
                max = use;

                scndCmd = topCmd;
                topCmd = cmd.getName();

            }else if(use > scndMax) {
                scndMax = use;
                scndCmd = cmd.getName();
            }
            sb.append("Command name: ").append(cmd.getName()).append(" | Uses: ").append(e.getClient().getCommandUses(cmd)).append("\n");
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.decode(ConfigUtil.getHex()));
        eb.setTitle("Statistics");

        JDA jda = e.getJDA();
        String line = "\n------------\n";

        String cmds = "Top used: **" + topCmd + "** | Uses: **" + max + "**\n" +
                      (scndCmd == null ? "" : "Second Used: **" + scndCmd + "** | Uses: **" + scndMax+"**\n") +
                      "[Commands List & Uses]("+OtherUtil.postToHaste(sb.toString())+")";

        String chnls = "Categories: **" + jda.getCategories().size() + "**\n" +
                       "Text Channels: **" + jda.getTextChannels().size() + "**\n" +
                       "Voice Channels: **" + jda.getVoiceChannelCache().size() + "**";

        String date = OtherUtil.getDate(e.getClient().getStartTime().toString());

        eb.setDescription(
                "**General:**\n"+ "Servers: **"+jda.getGuilds().size() + "**" + "\nUsers: **"+ jda.getUsers().size() + "**" + line+
                "**Commands:**\n" + cmds + line +
                "**Channels:**\n" + chnls + line +
                "**Start Time:** " + date
        );
        e.reply(eb.build());
    }
}
