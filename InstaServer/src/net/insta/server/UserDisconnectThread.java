package net.insta.server;

import java.util.ArrayList;

import net.insta.base.client.Client;
import net.insta.server.connection.User;

public class UserDisconnectThread implements Runnable{

	private Thread thread;
	
	public UserDisconnectThread() {
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void stop() {
		this.thread.stop();
	}
	
	@Override
	public void run() {
		while(true) {
			User user;
			for( Client client : new ArrayList<Client>(User.getClients()) ) {
				user = (User)client;
				if( (System.currentTimeMillis() - user.getLastPing()) > 7500 ) {
					client.printf("Client timed out "+user.getName()+" ("+(System.currentTimeMillis() - user.getLastPing())+"ms)");
					user.disconnect();
				}
			}
			
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
