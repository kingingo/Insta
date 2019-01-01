package net.insta.client;

import java.io.File;

import net.insta.base.InstaBase;
import net.insta.base.configuration.file.YamlConfiguration;
import net.insta.client.connection.Server;
import net.insta.client.connection.ServerListener;

public class Main {
	
	public static void loadYML() {
		InstaBase.configuration = YamlConfiguration.loadConfiguration(new File("properties.yml"));
		
		InstaBase.configuration.setIfNotExist("server", "server.statsmc.net");
		InstaBase.configuration.setIfNotExist("port", 2000);
		InstaBase.configuration.setIfNotExist("debug", false);
		InstaBase.configuration.save();
		
		InstaBase.PORT = InstaBase.configuration.getInt("port");
		InstaBase.SERVER_ADRESS = InstaBase.configuration.getString("server");
	}
	
	public static void exit() {
		InstaBase.configuration.save();
		Server.getInstance().disconnect(true);
		System.exit(0);
	}
	
	public static void init() {
		InstaBase.init();
		loadYML();
		new ServerListener();
		InstaBase.setPrefix("Client");
		InstaBase.printf("InstaClient starts... ");
		Server.getInstance(); //startet den Versuch eine Verbindung zum Server aufzubauen
	}
	
	public static void main(String[] args) {
		init();
	}
	
}
