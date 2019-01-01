package net.insta.base.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.InstaBase;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketSendEvent;
import net.insta.base.packets.Packet;
import net.insta.base.packets.client.AESKeyPacket;
import net.insta.base.packets.client.PingPacket;
import net.insta.base.packets.server.PongPacket;
import net.insta.base.packets.server.RSAPublicKeyPacket;
import net.insta.base.utils.Callback;

public class WriteThread implements Runnable {
	
	private LinkedBlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();
	private LinkedBlockingQueue<Callback<Boolean>> callbacks = new LinkedBlockingQueue<Callback<Boolean>>();
	private Client client;
	private DataOutputStream output;
	private Thread thread;
	@Getter
	@Setter
	private boolean active = false;
	
	public WriteThread(Client client) {
		try {
			this.client=client;
			this.output = new DataOutputStream(client.getSocket().getOutputStream());
			this.thread=new Thread(this);
			this.active=true;
			this.thread.start();
		} catch (IOException e) {
			this.client.disconnect();
			e.printStackTrace();
		}
	}
	
	public void write(Packet packet) {
		write(packet,b->{});
	}
	
	public void write(Packet packet,Callback<Boolean> callback) {
		synchronized (queue) {
			queue.add(packet);
			callbacks.add(callback);
		}
	}
	
	//Zum Schliessen des OutputStream und des Threads
	public void close() {
		active=false;
		if(output != null)
			try {
				output.close();
				output=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		this.thread.interrupt();
	}

	@Override
    public void run(){
    	while(isActive() && output!=null) {
    		Packet packet;
    		Callback<Boolean> callback;
    		while(!queue.isEmpty()) {
    			synchronized(queue) {
    				packet = queue.poll();
    				callback = callbacks.poll();
    			}
    			
				boolean written = write0(packet);
				callback.run(written);
    			if(!written) {
    				client.disconnect();
    			}
			}
    		
    		try {
				Thread.sleep(100);
			} catch (Exception e) {}
    	}
    }
    
    public boolean write0(Packet packet) {
		PacketSendEvent event = new PacketSendEvent(false, client, packet);
		EventManager.callEvent(event);
		
		if(!event.isCancelled()) {
			InstaBase.debug("["+client.toString()+"]: send to Client "+Packet.getPacketName(packet.getId()));
			byte[] packetBytes = packet.toByteArray();
			try {
				
				if(packet.toEncrypt()) {
					try {
						packetBytes = this.client.cipher.encrypt(packetBytes);
						output.writeInt(packetBytes.length);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					output.writeInt(packetBytes.length);
				}
				output.writeInt(packet.getId());
				output.write(packetBytes);
				output.flush();
			} catch (IOException e) {
				return false;
			}
		}
		return !event.isCancelled();
	}
  }
