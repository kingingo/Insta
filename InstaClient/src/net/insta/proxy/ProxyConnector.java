package net.insta.proxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import net.insta.base.InstaBase;
import net.insta.base.event.EventHandler;
import net.insta.base.event.EventListener;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.packets.server.ProxyPacket;

public class ProxyConnector implements EventListener {
	private ArrayList<ProxyConnection> proxies = new ArrayList<>();
	
	public ProxyConnector() {
		EventManager.register(this);
	}
	
	public void stop() {
		for(ProxyConnection proxy : proxies)
			proxy.close0();
		this.proxies.clear();
		EventManager.unregister(this);
	}
	
	public void removeProxy(ProxyConnection con) {
		this.proxies.remove(con);
	}
	
	@EventHandler
	public void receive(PacketReceiveEvent ev) {
		if(ev.getPacket() instanceof ProxyPacket) {
			ProxyPacket packet = (ProxyPacket)ev.getPacket();
			
			try {
				Socket socket = new Socket(InstaBase.configuration.getString("server"),packet.getPort());
				DataOutputStream o = new DataOutputStream(socket.getOutputStream());
				this.proxies.add(new ProxyConnection(socket,this));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
