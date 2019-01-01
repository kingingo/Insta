package net.insta.server.terminal.command;

import net.insta.base.InstaBase;
import net.insta.server.terminal.CommandExecutor;
import net.insta.server.terminal.Terminal;

public class DebugCommand implements CommandExecutor{

	@Override
	public void onCommand(String[] args) {
		InstaBase.setDebug(!InstaBase.isDebug());
		
		if(InstaBase.isDebug()) {
			InstaBase.printf("Debug messages enabled");
		} else {
			InstaBase.printf("Debug messages disabled");
		}
	}

	@Override
	public String getDescription() {
		return "enables debug messages";
	}

}
