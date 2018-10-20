package utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.uwetrottmann.tmdb2.entities.Genre;
import commands.family.Children;
import commands.family.Marriage;
import org.json.JSONArray;
import org.json.JSONObject;

import main.Bumblebot;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utility.core.UsrMsgUtil;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OtherUtil {

	private static final OkHttpClient client = new OkHttpClient();
	private static final String WEBHOOK_URL = ConfigUtil.getWebhookURL();
	private static final String JSONPOST = "application/json";
	private static final String TEXTPOST = "text/plain; charset=utf-8";
	
	public static boolean isValidURL(String url){
	    try {
	        new URL(url);
	        return true;
	    } catch (Exception ex){
	        return false;
	    }
	}
	
	public static void unZipFile(File zip, File extractTo) throws IOException {
		ZipFile archive = new ZipFile(zip);
		Enumeration<?> e = archive.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            File file = new File(extractTo, entry.getName());
            if (entry.isDirectory() && !file.exists()) {
                file.mkdirs();
            }else{
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
 
                InputStream in = archive.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
 
                byte[] buffer = new byte[8192];
                int read;

                while (-1 != (read = in.read(buffer))) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.close();
            }
        }
        archive.close();
    }

	@SuppressWarnings("SameParameterValue")
	private static String getPOST( String url, String bodyData) {
		try {
			MediaType mediaType = MediaType.parse(OtherUtil.TEXTPOST);
			RequestBody body = RequestBody.create(mediaType, bodyData);
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			return Objects.requireNonNull(response.body()).string();
		} catch (IOException e1) {
			return null;
		}
	}
	
	public static String getRandom(String... items) {
    	return items[(int)(Math.random()*items.length)];
    }
	
	public static void getWebhookError(Exception error, String className, User user) {
		StringWriter errors = new StringWriter();
		
		String avatar;
		try {
			avatar = Bumblebot.jda.getSelfUser().getAvatarUrl();
		}catch(NullPointerException ex) {
			avatar = "";
		}
		
		error.printStackTrace(new PrintWriter(errors));
		String last = "[Error description for class "+className+"]("+postToHaste(errors.toString())+")";
		String jsonString = new JSONObject().put("attachments", 
				new JSONArray().put(0, 
						new JSONObject().put("color", "#Cf0000")
						.put("author_name", error.getClass().getName())
						.put("author_icon", avatar)
						.put("text", last+(user==null?"":"\nTriggered by **" + user.getName() + " ("+user.getId()+")**"))
						.put("footer", className)
						.put("ts", Instant.now().getEpochSecond())))
				.toString();
		getJSONPOST(WEBHOOK_URL+"/slack", jsonString);
		
		if(!(user==null)) {
			user.openPrivateChannel().queue(m -> m.sendMessage("Something happpened with the command you triggered, will be reported. Sorry for the inconvenience!\n```Java\n"+error.getMessage()+"```").queue());
		}
	}
	
	private static void getJSONPOST(String url, String json) {
		try {
			MediaType JSON = MediaType.parse(JSONPOST);
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			Objects.requireNonNull(response.body()).string();
		} catch (IOException ignored) {}
	}
	
	public static String getGET(String url) {
		Request request = new Request.Builder().url(url).get().build();
		Response response;
		try {
			response = client.newCall(request).execute();
			return Objects.requireNonNull(response.body()).string();
		} catch (IOException e) {
			return null;
		}
	}

	public static String getUptime() {
		final long duration = ManagementFactory.getRuntimeMXBean().getUptime();
		final long d = duration / 86400000L % 30;
		final long h = duration / 3600000L % 24;
		final long m = duration / 60000L % 60;
		final long s = duration / 1000L % 60;

		return (d == 0 ? "" : d + " day")+(d == 0 | d == 1 ? "" : "s, ")+(d == 1 ? ", " : "")
				+ (h == 0 ? "" : h + " hour")+(h == 0 | h == 1 ? "" : "s, ")+(h == 1 ? ", " : "")
				+ (m == 0 ? "" : m + " minute")+(m == 0 | m == 1 ? "" : "s, ")+(m == 1 ? ", " : "")
				+ (s == 0 ? "" : s + " second")+(s == 0 | s == 1? "" : "s.")+(s == 1 ? ", " : "");
	}
	
	@SuppressWarnings("SameParameterValue")
	private static boolean isValidFormat(String format, String value) {
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
        	return false;
        }
        return date != null;
    }
	
	public static String getDate(String input) {
		if(isValidFormat("EEE MMM dd HH:mm:ss z yyyy", input)) {
			return ZonedDateTime.parse(input, DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")).format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
		}else{
			return OffsetDateTime.parse(input).format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
		}
	}
	
	public static String postToHaste(String body) {
		String val = getPOST("https://yourcousinwas.forsale/documents", body);
		return (val == null ? "POST is currently down" : "https://yourcousinwas.forsale/"+ new JSONObject(val).getString("key"));
	}
	
	public static String getCount(String input) {
		NumberFormat myFormat = NumberFormat.getInstance();
		double value = Double.parseDouble(input);
		myFormat.setGroupingUsed(true);
		input = myFormat.format(value) + "";
		return input;
	}
	
	public static String[] toStringArray(JSONArray array) {
	    if(array==null)
	        return null;

	    String[] arr=new String[array.length()];
	    for(int i=0; i<arr.length; i++) {
	        arr[i]=array.optString(i);
	    }
	    return arr;
	}

	public static String getGenreString(List<Genre> genres) {
		String genreText;
		if(genres == null) {
			genreText = "N/A";
		}else{
			StringBuilder gen = new StringBuilder();
			for(int i = 0; i < 3 && i < genres.size(); i++) {
				gen.append(genres.get(i).name.substring(0, 1).toUpperCase()).append(genres.get(i).name, 1, genres.get(i).name.length()).append(", ");
			}
			genreText = gen.toString().substring(0, gen.length()-2)+".";
		}
		return genreText;
	}

	public static User getUserArguement(CommandEvent e) {
		User user;
		if(e.getMessage().getMentionedUsers().size() > 0) {
			user = e.getMessage().getMentionedUsers().get(0);
		}else if(!e.getArgs().isEmpty()) {
			user = e.getJDA().retrieveUserById(e.getArgs()).complete();
		}else{
			user = e.getAuthor();
		}
		return user;
	}

	public static boolean childArguement(Children chl, String child, boolean isInGuild, CommandEvent e) {
		if(chl.isAdopted(child)) {
			if(isInGuild) {
				e.reply("You're already adopted by " + e.getJDA().getUserById(chl.getParentA(child)).getAsMention());
			}else{
				e.reply("You're already adopted by **" + UsrMsgUtil.getUserSet(e.getJDA(), chl.getParentA(child)) + "**");
			}
			return true;
		}
		return false;
	}

	public static boolean marriageArguement(Marriage mrg, String user, boolean isInGuild, CommandEvent e, String mStatus, boolean removeUser, boolean self) {
		String USER = e.getJDA().getUserById(user).getAsMention();

		if((mStatus.equals("married") ? mrg.isMarried(user) : mrg.isProposing(user))) {
			if(isInGuild) {
				if(removeUser) {
					e.reply(USER+" just divorced "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
				}else{
					e.reply(USER+" "+(self?"you're":" is")+" already "+(mStatus.equals("married") ? "married" : "proposing")+" to "+e.getJDA().getUserById(mrg.getPartner(user)).getAsMention());
				}
			}else{
				if(removeUser) {
					e.reply(USER+" just divorced **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
				}else{
					e.reply(USER+" "+(self?"you're":" is")+" already "+(mStatus.equals("married") ? "married" : "proposing")+" to **"+UsrMsgUtil.getUserSet(e.getJDA(), mrg.getPartner(user))+"**");
				}
			}

			if(removeUser) {
				mrg.removeUser(mrg.getPartner(user));
				mrg.removeUser(user);
			}
			return true;
		}
		return false;
	}
	
	public static boolean isOwners(MessageReceivedEvent s) {
		if(s.getAuthor().getId().equals(ConfigUtil.getOwnerId()))
			return true;
		if(ConfigUtil.getAdmins() == null)
			return false;
		for(String id : ConfigUtil.getAdmins())
			if(id.equals(s.getAuthor().getId())) 
				return true;
		return false;
	}
}
