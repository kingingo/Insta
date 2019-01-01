package net.insta.base.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.InstaBase;
import net.insta.base.encryption.ICipher;
import net.insta.base.packets.Packet;
import net.insta.base.packets.client.RequestPacket;
import net.insta.base.packets.response.Response;
import net.insta.base.packets.server.ServerDisconnectPacket;

public abstract class Client {
	@Getter
	private static ArrayList<Client> clients = new ArrayList<Client>();

	public static void add(Client client) {
		synchronized (clients) {
			clients.add(client);
		}
	}

	public static Client get(int userId) {
		synchronized (clients) {
			for (Client client : Client.getClients()) {
				if (client.getID() == userId)
					return client;
			}
			return null;
		}
	}

	public static void remove(Client client) {
		synchronized (clients) {
			clients.remove(client);
		}
	}

	public static void disconnectAll(String reason) {
		synchronized (clients) {
			for (int i = 0; i < clients.size(); i++)
				if (reason.isEmpty())
					clients.get(i).disconnect();
				else
					clients.get(i).disconnect(reason);

			clients.clear();
		}
	}

	@Getter
	protected Socket socket;
	@Getter
	@Setter
	protected boolean connected = false;
	@Getter
	protected ReadThread reader;
	@Getter
	protected WriteThread writer;
	@Getter
	@Setter
	protected long ping = -1;
	@Getter
	@Setter
	protected ICipher cipher;
	@Getter
	@Setter
	private ClientStatus status = ClientStatus.NOT_AUTHENTICATED;
	@Getter
	@Setter
	private int ID = 0;

	public Client() {
	}

	public Client(Socket socket) {
		this.socket = socket;

		this.reader = new ReadThread(this);
		this.writer = new WriteThread(this);

		this.connected = true;
		Client.add(this);
		printf("connected");
	}

	public void write(Packet packet) {
		writer.write(packet);
	}

	public <T extends Packet> Response<T> writeRequest(Packet packet) {
		RequestPacket request = new RequestPacket(packet);
		Response<T> response = new Response<T>(this, request.getUuid());
		write(request);
		return response;
	}

	public void disconnect0() {
		if (socket != null && socket.isConnected()) {
			try {
				reader.close();
				writer.close();
				socket.close();
				connected = false;
				printf("disconnected");
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect(String reason) {
		printf("kick: " + reason);
		writer.write0(new ServerDisconnectPacket(reason));
		disconnect();
	}

	public abstract void disconnect();

	public InetAddress getAdress() {
		return socket.getInetAddress();
	}

	public void printf(String msg) {
		System.out.println("[" + toString() + "]: " + msg);
	}

	public String toString() {
		if (socket != null) {
			return InstaBase.date() + "|" + getAdress().getHostAddress();
		} else {
			return InstaBase.date() + "|Unkown";
		}
	}
}
