package net.insta.client.connection;

import net.insta.GUI.GUIAPI;
import net.insta.base.InstaBase;
import net.insta.base.encryption.AESCipher;
import net.insta.base.encryption.RSACipher;
import net.insta.base.event.EventHandler;
import net.insta.base.event.EventListener;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.event.events.client.ServerConnectedEvent;
import net.insta.base.packets.client.AESKeyPacket;
import net.insta.base.packets.client.HandshakePacket;
import net.insta.base.packets.server.HandshakeAnswerPacket;
import net.insta.base.packets.server.RSAPublicKeyPacket;

public class ServerListener implements EventListener{

	public ServerListener() {
		EventManager.register(this);
	}
	
	@EventHandler
	public void connect(ServerConnectedEvent ev) {
		Server.getInstance().write(new HandshakePacket());
	}
	
	@EventHandler
	public void receive(PacketReceiveEvent ev) {
		Server server = (Server)ev.getClient();
		
		if(ev.getPacket() instanceof RSAPublicKeyPacket) {
			RSAPublicKeyPacket packet = (RSAPublicKeyPacket)ev.getPacket();
			RSACipher cipher = new RSACipher(packet.getPublicKey());
			
			try {
				server.write(new AESKeyPacket(cipher.encrypt( ((AESCipher)server.getCipher()).getSecret().getEncoded() )));
				GUIAPI.setActiveLoginButton(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(ev.getPacket() instanceof HandshakeAnswerPacket) {
			HandshakeAnswerPacket packet = (HandshakeAnswerPacket)ev.getPacket();
			
			if(!packet.isSuccess()) {
				//VERSION STIMMT NICHT!!
				InstaBase.printf("InstaClient Version stimmt nicht mehr!!");
			}
		}
	}
}
