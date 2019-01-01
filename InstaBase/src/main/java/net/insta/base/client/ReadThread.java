package net.insta.base.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.InstaBase;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.packets.Packet;
import net.insta.base.packets.response.ResponseListener;
import net.insta.base.packets.server.RSAPublicKeyPacket;
import net.insta.base.packets.server.ResponsePacket;

public class ReadThread implements Runnable {
	
	private Client client;
	private DataInputStream input;
	private Thread thread;
	@Getter
	@Setter
	private boolean active = false;
	@Getter
	private HashMap<UUID,ResponseListener> listeners = new HashMap<>();
	
	public ReadThread(Client client) {
		try {
			this.client=client;
			this.input = new DataInputStream(client.getSocket().getInputStream());
			this.active=true;
			this.thread=new Thread(this);
			this.thread.start();
		} catch (IOException e) {
			this.client.disconnect();
			e.printStackTrace();
		}
	}
	
	//Zum Schliessen des InputStream und des Threads
	public void close() {
		active=false;
		if(input != null)
			try {
				input.close();
				input=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		this.thread.interrupt();
	}
	
	//Wartet auf Packete
	@Override
    public void run(){
    	while(input!=null && isActive()) {
    		try {
    			//Falls Input verfuegbar ist wird available > 0 sein!
				if(input.available() > 0) {
					//Packet laenge
					int packetLength = input.readInt();
					
					if(packetLength < 0) {
						System.err.println("Die Laenge des Packets stimmt nicht! "+packetLength);
					}else {
						int packetId = input.readInt();
						
						if(client.getStatus() == ClientStatus.NOT_AUTHENTICATED && Packet.getPacketIdMax() < packetId) {
							InstaBase.debug("["+client.toString()+"]: tried to communicated but doesn't how!?");
							client.disconnect();
						} else {
							InstaBase.debug("["+client.toString()+"]: read from Client "+Packet.getPacketName(packetId));
							
							byte[] packetBytes = new byte[packetLength];
							input.readFully(packetBytes, 0, packetLength);
							
							if(Packet.toEncrypt(packetId)) {
								try {
									packetBytes = this.client.getCipher().decrypt(packetBytes);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
							Packet packet = Packet.create(packetId, packetBytes);
							
							if(packet instanceof ResponsePacket) {
								ResponsePacket response = (ResponsePacket)packet;
								ResponseListener listener = listeners.get(response.getUuid());
								
								if(listener!=null) {
									listeners.remove(response.getUuid());
									listener.handle(response.getUuid(),response.getPacket());
									EventManager.callEvent(new PacketReceiveEvent(client,response.getPacket()));
								}
							} else {
								EventManager.callEvent(new PacketReceiveEvent(client,packet));
							}
						}
					}
				}else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
  }
