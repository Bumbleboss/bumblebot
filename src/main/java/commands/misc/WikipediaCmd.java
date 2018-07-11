package commands.misc;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.EmbedBuilder;
import utility.OtherUtil;

public class WikipediaCmd extends Command {

	public WikipediaCmd() {
		this.name = "wiki";
		this.help = "Search on Wikipedia!";
		this.category = Bumblebot.Misc;
		this.arguments = "<query> {} breast";
	}
	@Override
	protected void execute(CommandEvent e) {
		if(e.getArgs().isEmpty()) {
			e.reply("You need to provide a query!");
			return;
		}
		
		String args = getPageId(e.getArgs()).toString();
		String parse = null;

		try {
			parse = OtherUtil.getGET("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&pageids="+URLEncoder.encode(args, "UTF-8"));
		} catch (UnsupportedEncodingException ex) {OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());}
		
		JSONObject json = new JSONObject(parse).getJSONObject("query").getJSONObject("pages");
		
		String id = null;
		for(String key: json.keySet()) {
			id = key;
		}
		
		JSONObject res = json.getJSONObject(id);
		
		if(id.equals("-1")) {
			e.reply("There are no articles with the given query");
		}else{
			String title = res.getString("title");
			String des = res.getString("extract");
			e.reply(new EmbedBuilder().setAuthor("Wikipedia: "+ title, "https://en.wikipedia.org/wiki/"+title.replace(" ", "_"), "http://tny.im/cJc")
					.setColor(Color.decode("#EDEDED"))
					.setDescription(des.length()>1900? des.substring(0, 1850) + "... [Read more](https://en.wikipedia.org/wiki/"+title.replace(" ", "_")+")" : des ).build());
		}
	}
	
	public Integer getPageId(String query) {
		String parse = null;
		Integer ptitle = null;
		try {
			parse = OtherUtil.getGET("https://en.wikipedia.org/w/api.php?action=query&list=search&utf8&format=json&srsearch="+URLEncoder.encode(query, "UTF-8"));
			ptitle =new JSONObject(parse).getJSONObject("query").getJSONArray("search").getJSONObject(0).getInt("pageid");
		} catch (Exception ex) {
			if(ex instanceof JSONException) {
				ptitle = -1;
			}else{
				OtherUtil.getWebhookError(ex, this.getClass().getName(), null);
			}
		}
		return ptitle;
	}
}
