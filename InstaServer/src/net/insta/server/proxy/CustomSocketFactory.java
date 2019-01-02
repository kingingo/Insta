/**
 * Copyright (C) 2016 Bruno Candido Volpato da Cunha (brunocvcunha@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.insta.server.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import net.insta.base.InstaBase;
import net.insta.base.client.Client;
import net.insta.server.connection.User;

public class CustomSocketFactory extends SocketFactory implements Runnable {
	private static CustomSocketFactory factory;

	public static SocketFactory getDefault() {
		if (factory == null)
			factory = new CustomSocketFactory();
		return factory;
	}

	public Thread thread;
	public boolean active = true;
	public int LISTEN;
	public CustomServerSocket listen;

	public CustomSocketFactory() {
		this(2001);
	}

	public CustomSocketFactory(int LISTEN) {
		this.LISTEN = LISTEN;
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public void run() {
		printf("ProxyServer started...");
		try {
			listen = new CustomServerSocket(this.LISTEN);

			SocksSocket socket;
			while (active) {
				socket = (SocksSocket) listen.accept();
				InstaBase.debug("proxy connected");
				socket.connectV4(new InetSocketAddress("instagram.com", 443));
				printf(socket.getInetAddress().getHostAddress() + " connected  ");
				add(socket);
			}

			listen.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Proxy get(String host) {
		synchronized (Client.getClients()) {
			User user;
			for (Client client : Client.getClients()) {
				user = (User) client;
				if (host.equalsIgnoreCase(user.getAdress().getHostAddress()))
					return user.getProxy();
			}
		}
		throw new NullPointerException("Couldn't find a proxy for " + host);
	}

	public void add(Socket socket) {
		synchronized (Client.getClients()) {
			User user;
			Proxy proxy;
			for (Client client : Client.getClients()) {
				user = (User) client;
				if (socket.getInetAddress().getHostAddress().equalsIgnoreCase(user.getAdress().getHostAddress())) {
					proxy = user.getProxy();
					proxy.getProxies().add(socket);
					proxy.getSemaphore().release();
					break;
				}
			}
		}
	}

	public void printf(String msg) {
		System.out.println("[ProxyCollector] " + msg);
	}

	public Socket createSocket() {
		printf("Error: createSocket() was called");
		return new Socket();
	}

	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		Proxy proxy = (Proxy) get(host);
		InstaBase.debug("Proxy required and found");
		return proxy.acquire(this.listen);
	}

	public Socket createSocket(InetAddress host, int port) throws IOException {
		return this.createSocket(host.getHostAddress(), port);
	}

	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		printf("Error: createSocket(String host, int port, InetAddress localHost, int localPort) was called");
		return new Socket(host, port, localHost, localPort);
	}

	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		printf("Error: createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) was called");
		return new Socket(address, port, localAddress, localPort);
	}

}