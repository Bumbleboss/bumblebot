package utility.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import utility.OtherUtil;

public class FileManager {
	
	private static String path;
	
	public FileManager(String path) {
		FileManager.path = path;
	}

	public void createFolder(String folderName) throws IOException {
		Files.createDirectory(Paths.get(path+folderName));
	}
	
	public void createFile(String fileName) throws IOException {
		Files.createFile(Paths.get(path+File.separator+fileName));
	}
	
	public void deleteFile(String fileName) throws IOException {
		Files.delete(Paths.get(path+File.separator+fileName));
	}
	
	public void deleteFolder(File file) {
		if(file.isDirectory()){	 
			if(file.list().length==0){
				file.delete();
			}else{
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);	        		 
					deleteFolder(fileDelete);
				}
				if(file.list().length==0){
					file.delete();
				}
			}
		}else{
			file.delete();
		}
	}
	
	
	public void writeFile(String fileName, String body) throws IOException {
		byte data[] = body.getBytes();
		FileOutputStream out = new FileOutputStream(fileName);
		out.write(data);
		out.close();
	}
	
	public void appendFile(String fileName, String body) throws IOException {
		FileWriter fl = new FileWriter(fileName, true);
		fl.append(body);
		fl.close();
	}
	
	public List<String> listFiles(String fileName, String fileFormat) {
		File fl = new File((path==null?"":path)+fileName);
		return Arrays.asList(fl.listFiles((path) -> path.getName().endsWith(fileFormat)))
                .stream().map(f -> f.getName()).collect(Collectors.toList());
	}
	
	public void moveFile(String oldDir, String newDir) throws IOException {
		Files.move(new File(path+File.separator+oldDir).toPath(), new File(path+File.separator+newDir).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
		try {
            FileReader fileReader = new FileReader((path==null?"":path+File.separator)+fileName);
            try(BufferedReader br = new BufferedReader(fileReader)) {
                for(String line; (line = br.readLine()) != null; ) {
                	sb.append(line+"\n");
                }
                br.close();         
            }
            fileReader.close();
            return sb.toString();
        }catch (Exception ex) {
        	if(ex instanceof FileNotFoundException) {
        		return "File was not found";
        	}else{
    			OtherUtil.getWebhookError(ex, FileManager.class.getName(), null);
    			return ex.getMessage();
        	}
        }
	}
	
	public static List<String> readFiles(String fileName) {
		List<String> lines = new ArrayList<String>();
		try {
            FileReader fileReader = new FileReader((path==null?"":path+File.separator)+fileName);
            try(BufferedReader br = new BufferedReader(fileReader)) {
                for(String line; (line = br.readLine()) != null; ) {
                	lines.add(line);
                }
                br.close();         
            }
            fileReader.close();
            return lines;
        }catch (Exception ex) {
        	if(ex instanceof FileNotFoundException) {
        		lines.add("File was not found");
        		return lines;
        	}else{
    			OtherUtil.getWebhookError(ex, FileManager.class.getName(), null);
    			return lines;
        	}
        }
	}
}
