package net.insta.server.connection;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.InstaBase;
import net.insta.base.client.Client;
import net.insta.base.client.ClientStatus;
import net.insta.server.connection.jobthread.InstagramJobThread;
import net.insta.server.proxy.Proxy;

public class User extends Client {	
	
	public static void disconnectAll(String reason) {
		synchronized (Client.getClients()) {
			for (int i = 0; i < Client.getClients().size(); i++)
				if(reason.isEmpty())
					((User)Client.getClients().get(i)).disconnect();
				else
					((User)Client.getClients().get(i)).disconnect(reason);

			Client.getClients().clear();
		}
	}
	
	@Getter
	@Setter
	private long lastPing = System.currentTimeMillis();
	@Getter
	private InstagramJobThread jobThread;
	@Setter
	private String email;
	@Getter
	@Setter
	private Proxy proxy;

	public User(Socket socket) {
		super(socket);
		this.jobThread=new InstagramJobThread(this);
		this.proxy= new Proxy(this);
	}

	public void disconnect() {
		super.disconnect0();
		remove(this);
		this.proxy.close();
		this.jobThread.logout();
		setStatus(ClientStatus.NOT_AUTHENTICATED);
	}
	
	public String getName() {
		if(email==null)return toString();
		return email.substring(0, email.indexOf("@"));
	}
	
	public String toString() {
		if(getID()!=0) {
			return InstaBase.date()+"|"+getName();
		}else {
			return super.toString();
		}
	}
}
