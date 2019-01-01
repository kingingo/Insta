package net.insta.base;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.SecretKey;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.configuration.file.YamlConfiguration;
import net.insta.base.encryption.AESCipher;
import net.insta.base.encryption.RSACipher;
import net.insta.base.log.Log;
import net.insta.base.packets.Packet;
import net.insta.base.packets.client.AESKeyPacket;

public class InstaBase {
	private static final SimpleDateFormat DATE_FORMAT_NOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String VERSION = "1.0";
	public static int PORT = 2000;
	public static String SERVER_ADRESS = "server.statsmc.net";
	public static YamlConfiguration configuration;
	@Setter
	private static String prefix = "Server/Client";
	/**
	 * Protocol:
	 * 
	 	*Client -> Server: Verbindungs aufbau! 
	 * 
	 * Verschlüsselung:
	 	* Server -> Client: RSAPublicKeyPacket übermittelt den PubliyKey
	 	* Client -> Server: AESKeyPacket übermittelt AES Key welcher durch den RSA PubliyKey verschlüsselt ist.
	 	* Server: Speichert den AES Key für die kommende Kommunikation die RSA Keys können verworfen werden!
	 * 
	 * Client Handshake:
		 * Client -> Server: HandshakePacket um die Client Version zu �berpr�fen!
		 * Server -> Client: HandshakeAnswerPacket ob Client version richtig ist oder falsch
		 * 			falls falsch -> Download link zum update
		 * 			falls richtig -> Client login panel aufrufen...
	 * 
	 * Client login try:
		 * Client -> Server LoginPacket
		 * Server -> Client LoginAnswerPacket
		 * 			falls loggedIn=true -> zum n�chsten panel
		 * 			falls loggedIn=false -> reason anzeigen und login panel nicht verlassen!
	 * 
	 * 
	 * Client send Ping:
		 * Client -> Server PingPacket
		 * Server -> Client PongPacket
		 * verbindung steht...
	 *
	 * Server Disconnect (Restart..etc)
	 	*Server -> Client: ServerDisconnectPacket.. Client begibt sich zm Login Panel 
	 */
	
	/**
	 * Beim Start des Programmes am Anfang ausf�hren damit alles geladen wird!
	 */
	public static void init() {
		Log.init();
		Packet.loadPackets();
	}
	
	public static String date() {
		return DATE_FORMAT_NOW.format(Calendar.getInstance().getTime());
	}
	
	public static boolean isDebug() {
		return configuration.getBoolean("debug");
	}
	
	public static void setDebug(boolean b) {
		configuration.set("debug", b);
	}
	
	public static void debug(String message) {
		if(InstaBase.isDebug())System.out.println(message);
	}
	
	public static void printf(String msg) {
		System.out.println("["+date()+"|"+prefix+"]: "+msg);
	}
	
	public static void printfErr(String msg) {
		System.err.println("["+date()+"|"+prefix+"]: "+msg);
	}
}
