package net.insta.server.terminal;

public interface CommandExecutor {
	public default String getCommand() {
		return getClass().getSimpleName().toLowerCase().replaceAll("command", "");
	}
	
	public String getDescription();
	
	public void onCommand(String[] args);
}
