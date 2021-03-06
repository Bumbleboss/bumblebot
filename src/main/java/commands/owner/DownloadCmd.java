package commands.owner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.Bumblebot;
import net.dv8tion.jda.core.entities.Message.Attachment;
import utility.OtherUtil;
import utility.core.FileManager;
import utility.core.UsrMsgUtil;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DownloadCmd extends Command {
	
	public DownloadCmd() {
		this.name = "download";
		this.help = "Downloads files & folders";
		this.ownerCommand = true;
		this.category = Bumblebot.Owner;
	}

	@Override
	protected void execute(CommandEvent e) {
		
		String args = e.getArgs().isEmpty()?"":e.getArgs();
		
		try {
			e.getMessage().getAttachments().get(0);
		}catch(IndexOutOfBoundsException ex) {
			UsrMsgUtil.sendEMessage("You need to provide an attachment to download", e.getChannel());
			return;
		}
		boolean zipFile = false;
        String message = null;
        try {
			Attachment att = e.getMessage().getAttachments().get(0);
			String fileName = att.getFileName();
			FileUtils.copyInputStreamToFile(att.getInputStream(), new File(args+fileName));
			FileManager file = new FileManager("./");
			List<String> list = file.listFiles("", "");
			for (String fl:list) {
				File fil = new File((fileName.contains(".zip")?args+fileName.replace(".zip", ""):args+fileName));
				if(fl.equals(args+fileName)) {
					if(fil.isDirectory()) {
						file.deleteFolder(fil);
						message = "deleted folder **" + fil.getName()+"**";
					}else if(fil.isFile()) {
						file.deleteFile(args+fileName);
                        message = "deleted file **" + fil.getName()+"**";
                    }
				}
			}

			if(fileName.contains(".zip")) {
				zipFile =  true;
				new Thread(() -> {
                    try {
                        OtherUtil.unZipFile(new File(args+fileName), new File(args.isEmpty()?"./":args));
                        file.deleteFile(args+fileName);
                    } catch (IOException ex) {
                        UsrMsgUtil.sendEMessage("Error:\n```"+ex.getLocalizedMessage()+"```", e.getChannel());
                    }
                }).start();
			}
		}catch(Exception ex) {
			OtherUtil.getWebhookError(ex, this.getClass().getName(), e.getAuthor());
			return;
		}
		UsrMsgUtil.sendVEMessage("Downloaded " + (zipFile?"& unzipped ": "") + "file" + (message != null?", also "+message+"!":"!"), e.getChannel());
	}
}
