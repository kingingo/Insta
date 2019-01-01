package net.insta.client.connection;

import net.insta.base.event.EventHandler;
import net.insta.base.event.EventListener;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.packets.client.PingPacket;
import net.insta.base.packets.server.PongPacket;

public class PingHandle implements EventListener, Runnable {

	/**
	 * Zeit wann PingPacket als letztes abgeschickt wurde
	 */
	private long lastPing = -1;
	private long lastPong = -1;
	private boolean lastPongReceived = true;
	/**
	 * Zeit lastPing - PongPacket.Ping = PING also Abschick_Zeit - Empfangungs_Zeit
	 * = PING
	 */
	private boolean active = true;
	private final Server server;
	private Thread thread;

	public PingHandle(Server server) {
		this.server = server;
		this.thread = new Thread(this);
		start();
		EventManager.register(this);
	}

	public void start() {
		this.active = true;
		this.thread.start();
	}

	public void stop() {
		this.active = false;
		EventManager.unregister(this);
	}

	// Sendet ein PingPacket zum Server
	public void ping() {
		this.lastPongReceived = false;
		this.lastPing = System.currentTimeMillis();
		this.server.write(new PingPacket(server.getPing()));
	}

	/**
	 * Wird ausgefuehrt wenn ein PongPacket ankommt
	 * 
	 * @param ev
	 */
	@EventHandler
	public void receive(PacketReceiveEvent ev) {
		if (ev.getPacket() instanceof PongPacket) {
			this.lastPongReceived = true;
			this.lastPong = System.currentTimeMillis();
			ev.getClient().setPing(this.lastPong - this.lastPing);
		}
	}

	@Override
	public void run() {
		while (active) {
			try {
				Thread.sleep(1000 * 3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (this.lastPongReceived) {
				ping();
			} else {
				server.printf("Lost connection to Server... disconnect" + " (Last Ping:"
						+ (System.currentTimeMillis() - this.lastPing) + "ms, Last Pong: "
						+ (System.currentTimeMillis() - this.lastPong) + ")");
				this.active = false;
			}
		}
		server.disconnect();
	}

}
