package net.insta.server.terminal.command;

import net.insta.server.Main;
import net.insta.server.terminal.CommandExecutor;

public class ExitCommand implements CommandExecutor{

	@Override
	public String getDescription() {
		return "for stopping the server";
	}

	@Override
	public void onCommand(String[] args) {
		if(args.length==0) {
			Main.exit();
		}else {
			Main.exit(args[0]);
		}
	}

}
