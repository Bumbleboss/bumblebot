package commands.misc;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.ConfigUtil;
import utility.OtherUtil;
import utility.core.UsrMsgUtil;
import yandexAPI.YandexAPI;
import yandexAPI.YandexLanguage;
import yandexAPI.YandexResponse;

public class TranslateCmd extends Command{
	
	public TranslateCmd() {
		this.name = "translate";
		this.help = "Translate a text!";
		this.category = Bumblebot.Misc;
		this.arguments = "<langto>; <text> {} fr; Hello!";
	}
	
	@Override
	protected void execute(CommandEvent e) {
		try {
			YandexAPI api = ConfigUtil.trns;
			String[] args = e.getArgs().split(";");
			
			YandexLanguage lng = null;
			YandexLanguage[] list = YandexLanguage.values();
            for (YandexLanguage aList : list) {
                if (aList.toString().equals(args[0])) {
                    lng = aList;
                }
            }
			
			YandexResponse yn = api.getYandexResponse(args[1], lng);
			e.reply(new EmbedBuilder()
					.addField("Sentence", args[1], false)
					.addField("Translation",yn.getText().get(0), false)
					.setColor(Color.decode(ConfigUtil.getHex()))
					.setFooter("Translated from "+ yn.getLang().toUpperCase()+" | Translation by Yandex.", null)
					.build());
			
		} catch (Exception ex) {
			if(ex instanceof ArrayIndexOutOfBoundsException) {
				UsrMsgUtil.sendEMessage("Usage: **"+ConfigUtil.getPrefix()+this.name + " "+ this.arguments.split("\\{}")[0] + "**", e.getChannel());
			}else{
				UsrMsgUtil.sendEMessage(OtherUtil.getAPIError(ex), e.getChannel());
				OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			}
		}	
	}
}
