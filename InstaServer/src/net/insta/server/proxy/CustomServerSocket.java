package net.insta.server.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class CustomServerSocket extends ServerSocket {

	public CustomServerSocket() throws IOException {
		super();
	}

	public CustomServerSocket(int port) throws IOException {
		super(port);
	}

	@Override
	public Socket accept() throws IOException {
		if (isClosed())
			throw new SocketException("Socket is closed");
		if (!isBound())
			throw new SocketException("Socket is not bound yet");
		SocksSocket s = SocksSocket.create();
		implAccept(s);
		return s;
	}

}
