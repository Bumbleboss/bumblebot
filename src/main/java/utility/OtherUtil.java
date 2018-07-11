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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

public class OtherUtil {

	private static OkHttpClient client = new OkHttpClient();
	public static String WEBHOOK_URL = ConfigUtil.getWebhookURL();
	public static String WEBHOOK_ERROR_URL = ConfigUtil.getWebhookErrorURL();
	public static String JSONPOST = "application/json";
	public static String TEXTPOST = "text/plain; charset=utf-8";
	
	public static boolean isValidURL(String url){
	    try {
	        new URL(url);
	        return true;
	    } catch (Exception ex){
	        return false;
	    }
	}
	
	public static final void unZipFile(File zip, File extractTo) throws IOException {
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
	
	public static String getAPIError(Exception e) {
		String error = e.getMessage().substring(e.getMessage().lastIndexOf("[Error]: "), e.getMessage().length()).replace("[Error]: ", "");
		String err = error.substring(0, error.lastIndexOf("[Error Description]: "));
		String error2 = error.substring(error.lastIndexOf("[Error Description]: "), error.length()).replace("[Error Description]: ", "");
		return "**Error:** "+err+"\n**Description:** " + error2;
	}
	
	public static String getPOST(String url, String bodyData, String mediaTp) {
		try {
			MediaType mediaType = MediaType.parse(mediaTp);
			RequestBody body = RequestBody.create(mediaType, bodyData);
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e1) {
			return null;
		}
	}
	
	public static String getRandom(String... items) {
    	return items[(int)(Math.random()*items.length)];
    }
	
	public static void getWebhookError(Exception error, String className, User user) {
		StringWriter errors = new StringWriter();
		
		String avatar = null;
		try {
			avatar = Bumblebot.jda.getSelfUser().getAvatarUrl();
		}catch(NullPointerException ex) {
			avatar = "";
		}
		
		error.printStackTrace(new PrintWriter(errors));
		String last = "[Error description for class "+className+"]("+postToHaste(errors.toString())+")";
		String jsonString = new JSONObject().put("attachments", 
				new JSONArray().put(0, 
						new JSONObject().put("color", ConfigUtil.getHex())
						.put("author_name", error.getClass().getName())
						.put("author_icon", avatar)
						.put("text", last+(user==null?"":"\nTriggered by **" + user.getName() + " ("+user.getId()+")**"))
						.put("footer", className)
						.put("ts", Instant.now().getEpochSecond())))
				.toString();
		getJSONPOST(WEBHOOK_ERROR_URL+"/slack", jsonString);
		
		if(!(user==null)) {
			user.openPrivateChannel().queue(m -> {
				m.sendMessage("Something happpened with the command you triggered, will be reported. Sorry for the inconvenience!\n```Java\n"+error.getMessage()+"```").queue();
			});
		}
	}
	
	public static String getJSONPOST(String url, String json) {
		try {
			MediaType JSON = MediaType.parse(JSONPOST);
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String getGET(String url) {
		Request request = new Request.Builder().url(url).get().build();
		Response response;
		try {
			response = client.newCall(request).execute();
			return response.body().string();
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
		
		String uptime = (d == 0 ? "" : d + " day")+(d == 0 | d == 1 ? "" : "s, ")+(d == 1 ? ", " : "")
				+ (h == 0 ? "" : h + " hour")+(h == 0 | h == 1 ? "" : "s, ")+(h == 1 ? ", " : "")
				+ (m == 0 ? "" : m + " minute")+(m == 0 | m == 1 ? "" : "s, ")+(m == 1 ? ", " : "")
				+ (s == 0 ? "" : s + " second")+(s == 0 | s == 1? "" : "s.")+(s == 1 ? ", " : "");
		return uptime;
	}
	
	public static void restart(){
		try {	
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar;
	
			currentJar = new File(Bumblebot.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			if(!currentJar.getName().endsWith(".jar")) {
				System.out.println("Woops");	
				return;
			}

			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
			try {Bumblebot.jda.shutdown();}catch (NullPointerException ex) {}
			System.exit(0);
		} catch (Exception ex) {
			OtherUtil.getWebhookError(ex, OtherUtil.class.getName(), null);
		}
	}
	
	public static boolean isValidFormat(String format, String value) {
        Date date = null;
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
		return "https://hastebin.com/"+ new JSONObject(getPOST("https://hastebin.com/documents", body, TEXTPOST)).getString("key");
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
