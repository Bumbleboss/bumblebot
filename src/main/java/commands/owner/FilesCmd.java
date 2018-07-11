package commands.owner;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import utility.OtherUtil;
import utility.core.FileManager;
import utility.core.UsrMsgUtil;

public class FilesCmd extends Command {

	public FilesCmd() {
		this.name = "file";
		this.help = "Edit, remove, do something to a file";
		this.aliases = new String[] {"fl", "files"};
		this.category = Bumblebot.Owner;
		this.hidden = true;
		this.ownerCommand = true;
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String[] arg = e.getArgs().split("\\;");
		try {
			String action = arg[0];
			String file = arg[1];
			
			FileManager fl = new FileManager("./");
			if(action.equalsIgnoreCase("create")) {
				fl.createFile(file);
				UsrMsgUtil.sendVEMessage("Created **"+file+"** folder", e.getChannel());
			}else if(action.equalsIgnoreCase("createfl")){
				fl.createFolder(file);
				UsrMsgUtil.sendVEMessage("Created **"+file+"**", e.getChannel());
			}else if(action.equalsIgnoreCase("delete")) {
				fl.deleteFile(file);
				UsrMsgUtil.sendVEMessage("Deleted **"+file+"**", e.getChannel());
			}else if(action.equalsIgnoreCase("deletefl")) {
				File fls = new File(file);
				fl.deleteFolder(fls);
				UsrMsgUtil.sendVEMessage("Deleted **"+file+"** folder", e.getChannel());
			}else if(action.equalsIgnoreCase("write")) {
				fl.writeFile(file, arg[2]);
				UsrMsgUtil.sendVEMessage("Edited the file **"+file+"** with a body of ```"+arg[2]+"```", e.getChannel());
			}else if(action.equalsIgnoreCase("append")) {
				fl.appendFile(file, "\n"+arg[2]+"\n");
				UsrMsgUtil.sendVEMessage("Edited the file **"+file+"** with a body of ```"+arg[2]+"```", e.getChannel());
			}else if(action.equalsIgnoreCase("move")) {
				fl.moveFile(file, arg[2]);
				UsrMsgUtil.sendVEMessage("Moved **"+file+"** to **" + arg[2]+"**", e.getChannel());
			}else if(action.equalsIgnoreCase("list")) {
				String format = null;
				try {format = arg[2];}catch(ArrayIndexOutOfBoundsException ex) {format = "";}
				List<String> ss = fl.listFiles(file, format);
				StringBuilder sb = new StringBuilder();
				StringBuilder sb2 = new StringBuilder();
				for(int i = 0; i < ss.size(); i++) {
					sb.append("**"+(i+1)+"** " + ss.get(i) + "\n");
					sb2.append(ss.get(i) + "\n");
				}
				if(sb.toString().length() > 1900) {
					UsrMsgUtil.sendVEMessage("[List of files]("+OtherUtil.postToHaste(sb2.toString())+")", e.getChannel());
				}else{
					UsrMsgUtil.sendVEMessage("**__List of files__:**\n"+sb.toString(), e.getChannel());
				}
			}else if(action.equalsIgnoreCase("read")) {
				String contents = FileManager.readFile(file);
				if(contents.length() > 1900) {
					UsrMsgUtil.sendVEMessage("[File contents]("+OtherUtil.postToHaste(contents)+")", e.getChannel());
				}else{
					UsrMsgUtil.sendVEMessage("Contents of the file **"+file+"**:\n```" +contents+"```", e.getChannel());
				}				
			}
		}catch(Exception ex) {
			if(ex instanceof ArrayIndexOutOfBoundsException) {
				UsrMsgUtil.sendEMessage("You forgot to provide the needed fields\n**ACTION;FILE?FORMAT;?BODYDATA**!", e.getChannel());
			}else{
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				UsrMsgUtil.sendEMessage("[Error description for "+ex.getClass().getName()+"]("+OtherUtil.postToHaste(errors.toString())+")", e.getChannel());
			}
		}
	}
}