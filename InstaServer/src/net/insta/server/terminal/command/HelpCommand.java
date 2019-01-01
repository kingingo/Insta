package net.insta.server.terminal.command;

import net.insta.base.InstaBase;
import net.insta.server.terminal.CommandExecutor;
import net.insta.server.terminal.Terminal;

public class HelpCommand implements CommandExecutor{

	@Override
	public void onCommand(String[] args) {
		if(Terminal.getCommands().isEmpty()) {
			InstaBase.printf("No commands");
		}else {
			for(CommandExecutor cmd : Terminal.getCommands()) {
				if(cmd.getDescription()!=null) {
					InstaBase.printf(cmd.getCommand()+": "+cmd.getDescription());
				}
			}
		}
	}

	@Override
	public String getDescription() {
		return null;
	}

}
