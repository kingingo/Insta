package net.insta.server.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import lombok.Getter;
import net.insta.base.packets.server.ProxyPacket;
import net.insta.server.connection.User;

public class Proxy {
	@Getter
	private Queue<Socket> proxies = new LinkedList<Socket>();
	@Getter
	private Semaphore semaphore = new Semaphore(0);
	private User user;
	
	public Proxy(User user) {
		this.user=user;
	}
	
	public Socket acquire(ServerSocket s) {
		if(proxies.isEmpty()) {
			user.getWriter().write0(new ProxyPacket(s.getLocalPort()));
		}
		
		try {
			this.semaphore.acquire();
			return proxies.poll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void close() {
		for(Socket s : proxies)
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		semaphore = new Semaphore(0);
	}
}
