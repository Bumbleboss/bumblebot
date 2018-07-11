package utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import utility.jdu.CountingArray;


public class ImgUtil {

	public static Color getMostCommonColour(BufferedImage image) throws Exception{
	    CountingArray<Integer> rgb = new CountingArray<>();
	    for(int y = 0; y < image.getHeight(); y++) {
	    	for(int x = 0; x < image.getWidth(); x++) {
	    		rgb.add(image.getRGB(x, y));	
	    		if(rgb.size() > 10) {
	    			throw new Exception("Too many common colors");
	    		}
	    	}
	    }
	    return new Color(rgb.getMostPopularEntry());
	}
	
	public static InputStream imageFromUrl(String url){
        if(url==null)
            return null;
        try {
            URL u = new URL(url);
            URLConnection urlConnection = u.openConnection();
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
            return urlConnection.getInputStream();
        } catch(IOException|IllegalArgumentException e) {
            return null;
        }
    }
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    if(image != null) {
	        if(image.getWidth() != width && image.getHeight() != height) {
	            try {
	                Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	                image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	                Graphics2D g = image.createGraphics();
	                g.drawImage(temp, 0, 0, null);
	                g.dispose();
	                return image;
	            }catch(Exception e) {
	                e.printStackTrace();
	                return null;
	            }
	        }else{
	            return image;
	        }
	    }else{
	        return null;
	    }
	}
}

