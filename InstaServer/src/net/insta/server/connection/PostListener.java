package net.insta.server.connection;

import net.insta.base.event.EventHandler;
import net.insta.base.event.EventListener;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.packets.client.LoadPostPacket;
import net.insta.base.packets.server.ResponsePacket;

public class PostListener implements EventListener{

	public PostListener() {
		EventManager.register(this);
	}
	
	@EventHandler
	public void loadPost(PacketReceiveEvent ev) {
		if(ev.getPacket() instanceof ResponsePacket) {
			ResponsePacket response = (ResponsePacket)ev.getPacket();
			
		}
	}
	
}
