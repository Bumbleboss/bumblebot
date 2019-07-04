package commands.myServer;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.myServer.roles.RolesManager;
import commands.myServer.rules.RulesManager;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;

import java.awt.*;
import java.util.List;

public class ServerCmd extends Server{

    public ServerCmd() {
        this.name = "myserver";
        this.help = "Sends all the information about the server";
        this.ownerCommand = true;
    }

    public void doCommand(CommandEvent e) {
        String aboutText = "Welcome to BumbleCore's server! In this channel, " +
                "you will get to know what all the channels are about and rules that abides them " +
                "and also all the roles that you may assign for yourself!\n\nTo invite others:\nhttps://discord.gg/7PCdKYN";
        sendMessage(e, aboutText, "https://cdn.discordapp.com/attachments/339727721376645120/518015097176588299/sample_server_banner.png");

        List<Object> rolesList = RolesManager.getRoles().toList();
        StringBuilder sb = new StringBuilder();

        sb.append("Roles List:\n");
        for(int i = 0; i < rolesList.size(); i++) {
            if(RolesManager.getRoleType(i).equals("coding")) {
                sb.append("**" + RolesManager.getRoleName(i) + "** | ");
            }else{
                sb.append("***" + RolesManager.getRoleName(i) + "*** - *" + RolesManager.getRoleDes(i) +"*\n");
            }
        }
        sendMessage(e, sb.toString(), "https://cdn.discordapp.com/attachments/312197256399028224/518018662742097922/2018-11-30_14-00-41.gif");

        List<Object> channelsList = RulesManager.getChannels().toList();
        StringBuilder sb2 = new StringBuilder();

        sb2.append("Channels List:\n");
        for(int i = 0; i < channelsList.size(); i++) {
            sb2.append(RulesManager.getChannelName(i)+"\n");
        }
        sendMessage(e, sb2.toString(), "https://cdn.discordapp.com/attachments/339727721376645120/518020348873277440/sample_channels.png");
    }

    public void sendMessage(CommandEvent e, String text, String image) {
        e.reply(new EmbedBuilder().setColor(Color.decode(ConfigUtil.getHex())).setDescription(text).setImage(image).build());
    }
}
