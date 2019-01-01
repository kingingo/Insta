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
package net.insta.server.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import net.insta.base.client.Client;
import net.insta.server.connection.User;

public class CustomTestSocketFactory extends SocketFactory {

	private static CustomTestSocketFactory factory;
	
	public static SocketFactory getDefault() {
		if(factory==null)factory=new CustomTestSocketFactory();
		return factory;
	}

	public void printf(String msg) {
		System.out.println("[ProxyCollector] " + msg);
	}

	public Socket createSocket() {
		printf("Error: createSocket() was called");
		return new Socket();
	}
	
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		printf("Error: createSocket(String host, int port) was called");
		return new Socket();
	}

	public Socket createSocket(InetAddress host, int port) throws IOException {
		return this.createSocket(host.getHostAddress(),port);
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