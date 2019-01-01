package net.insta.server.terminal;

import java.util.ArrayList;

import net.insta.base.InstaBase;

public class Terminal implements Runnable{
	private static Terminal instance;
	private static ArrayList<CommandExecutor> commands = new ArrayList<>();
	
	public static Terminal getInstance() {
		if(instance==null)instance=new Terminal();
		return instance;
	}
	
	public static ArrayList<CommandExecutor> getCommands(){
		return commands;
	}
	
	public static void register(CommandExecutor cmd) {
		if(!commands.contains(cmd)) {
			commands.add(cmd);
		}
	}
	
//	private BufferedReader input;
	private Thread thread;
	private boolean active = true;
	
	private Terminal() {
//		this.input = new BufferedReader(new InputStreamReader(System.in));
		this.thread=new Thread(this);
		this.thread.start();
	}
	
	public void stop() {
		this.active=false;
		this.thread.stop();
	}
	
	private boolean onCommand(String line) {
		String[] splitted = line.split(" ");
		if(splitted.length > 0) {
			String command = splitted[0];
			String[] args = new String[splitted.length-1];
			
			for(int i = 1; i < splitted.length; i++)args[i-1]=splitted[i];
			
			for(CommandExecutor cmd : commands) {
				if(cmd.getCommand().equalsIgnoreCase(command)) {
					cmd.onCommand(args);
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@Override
	public void run() {
		while(this.active) {
			try {
				String line;
//				while( (line=input.readLine())!= null ) {
				while( (line=System.console().readLine())!= null ) {
					if(!this.onCommand(line)) {
						InstaBase.printf("Command not found "+line);
					}
				}

				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
