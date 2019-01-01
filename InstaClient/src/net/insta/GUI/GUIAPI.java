package net.insta.GUI;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import net.insta.base.packets.client.LoginPacket;
import net.insta.base.packets.client.instagram.InstagramAuthDataPacket;
import net.insta.base.packets.client.instagram.InstagramLikeLastPostPacket;
import net.insta.base.packets.client.instagram.InstagramTestPacket;
import net.insta.base.packets.response.Response;
import net.insta.base.packets.server.LoginAnswerPacket;
import net.insta.base.packets.server.instagram.InstagramAuthDataAnswerPacket;
import net.insta.base.packets.server.instagram.InstagramTestAnswerPacket;
import net.insta.client.connection.Server;

public class GUIAPI {
	
	/**
	 * MainGUI öffnet sich zweimal evt falls verbindung verloren und wiederherrgestellt
	 * ProxyServer startet zweimal? test
	 */
	
	/**
	 * 
	 * @param c = true -> Login Button aktive
	 * 		  c = false -> Login Button deaktiviert
	 */
	public static void setActiveLoginButton(boolean c) {
		LoginGUI.getInstance().getLogin().setDisable(!c);
		
		if(c) {
			LoginGUI.getInstance().getMessage().setFill(Color.GREEN);
			LoginGUI.getInstance().getMessage().setText("Server connected");
		}else {
			LoginGUI.getInstance().getMessage().setFill(Color.FIREBRICK);
			LoginGUI.getInstance().getMessage().setText("Waiting for Server to connect");
		}
	}
	
	public static void sendList(boolean sim,ArrayList<String> accounts, ArrayList<String> comments) {
		Server.getInstance().write(new InstagramLikeLastPostPacket(sim,accounts,comments));
	}
	
	public static LoginAnswerPacket login(String mail, String pass) {
		Response<LoginAnswerPacket> response = Server.getInstance().writeRequest(new LoginPacket(mail,pass));
		LoginAnswerPacket packet = response.getSync();
		
		if(packet.isSuccess()) {
			Server.getInstance().setID(packet.getUserId());
		}
		
		return packet;
	}
	
	public static InstagramTestAnswerPacket test(String shortcode, long timeout) {
		Response<InstagramTestAnswerPacket> response = Server.getInstance().writeRequest(new InstagramTestPacket(shortcode));
		InstagramTestAnswerPacket packet = response.getSync(timeout);
		return packet;
	}
	
	public static InstagramAuthDataAnswerPacket authData(String username, String pass) {
		Response<InstagramAuthDataAnswerPacket> response = Server.getInstance().writeRequest(new InstagramAuthDataPacket(username, pass));
		InstagramAuthDataAnswerPacket packet = response.getSync(1000*10*6);
		return packet;
	}

}
