package net.insta.client.connection;

import java.io.IOException;
import java.net.Socket;

import lombok.Getter;
import net.insta.base.InstaBase;
import net.insta.base.client.Client;
import net.insta.base.client.ReadThread;
import net.insta.base.client.WriteThread;
import net.insta.base.encryption.AESCipher;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.client.ServerConnectedEvent;
import net.insta.base.event.events.client.ServerDisconnectEvent;
import net.insta.base.packets.Packet;
import net.insta.base.packets.client.RequestPacket;
import net.insta.base.packets.response.Response;
import net.insta.proxy.ProxyConnector;

public class Server extends Client {

	private static Server instance;

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
			instance.connect();
		}
		return instance;
	}

	private Server() {
		this.cipher = new AESCipher();
	}

	private Thread connectionThread;
	private PingHandle pingHandle;
	@Getter
	private ProxyConnector proxy;
	
	public void connect() {
		this.connectionThread = new Thread(new ConnectionThread());
		this.connectionThread.start();
	}

	public void disconnect() {
		this.disconnect(false);
	}
	
	public void disconnect(boolean full) {
		if(this.pingHandle!=null) {
			this.pingHandle.stop();
			this.pingHandle=null;
		}
		
		if(proxy!=null) {
			this.proxy.stop();
			this.proxy=null;
		}
		if(!full)this.connect();
		EventManager.callEvent(new ServerDisconnectEvent());
		super.disconnect0();
	}

	public boolean connect0() {
		try {
			printf("try to connect to "+InstaBase.SERVER_ADRESS+":"+InstaBase.PORT);
			this.socket = new Socket(InstaBase.SERVER_ADRESS, InstaBase.PORT);
			add(this);

			this.proxy=new ProxyConnector();
			this.reader = new ReadThread(this);
			this.writer = new WriteThread(this);

			printf("Verbindung zum Server hergestellt! " + InstaBase.SERVER_ADRESS + ":" + InstaBase.PORT);
			this.pingHandle = new PingHandle(this);
			this.connected = true;
			EventManager.callEvent(new ServerConnectedEvent());
			return true;
		} catch (IOException e) {
			connected = false;
			printf("couldn't connect to the server");
		}
		return false;
	}

	// Soll alle 5sekunden versuchen eine Verbindung zum Server aufzubauen
	private class ConnectionThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000 * 3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (connect0())
					break;
			}
		}
	}
}