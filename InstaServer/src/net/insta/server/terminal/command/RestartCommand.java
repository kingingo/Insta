package net.insta.server.terminal.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import net.insta.base.InstaBase;
import net.insta.server.Main;
import net.insta.server.terminal.CommandExecutor;

public class RestartCommand implements CommandExecutor {

	@Override
	public void onCommand(String[] args) {
		restart(new Runnable() {

			@Override
			public void run() {
				Main.exit();
			}
		});
	}

	@Override
	public String getDescription() {
		return "restarts the server";
	}

	public void restart(Runnable beforeRestart) {
		File file = new File("run.sh");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileOutputStream(file));
				out.write("screen -dmS insta java -jar "+System.getProperty("user.dir")+File.separator+"InstaServer.jar -Xmx 2048m");
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (out != null)
					out.close();
			}
			
			try {
				Runtime.getRuntime().exec("chmod 777 run.sh");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(file.exists()) {
			Thread hook = new Thread(){
				public void run(){
					try {
						Runtime.getRuntime().exec("./run.sh");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			hook.setDaemon(true);
			Runtime.getRuntime().addShutdownHook(hook);
		} else {
			InstaBase.printfErr("Couldn't find run.sh");
		}
		
		beforeRestart.run();
		System.exit(0);
	}
}
