package net.insta.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import net.insta.base.InstaBase;
import net.insta.base.configuration.file.YamlConfiguration;
import net.insta.server.connection.User;
import net.insta.server.connection.UserListener;
import net.insta.server.mysql.MySQL;
import net.insta.server.proxy.CustomSocketFactory;
import net.insta.server.terminal.Terminal;
import net.insta.server.terminal.command.DebugCommand;
import net.insta.server.terminal.command.ExitCommand;
import net.insta.server.terminal.command.HelpCommand;
import net.insta.server.terminal.command.RestartCommand;
import net.insta.server.test.TestCommand;

public class Main {
	public static UserDisconnectThread disconnectThread;
	public static ServerSocket server;
	
	public static void init() {
		new UserListener();
		disconnectThread = new UserDisconnectThread();
		loadYML();
		loadMySQL();
		registerCommands();
		CustomSocketFactory.getDefault();
	}
	
	public static void registerCommands() {
		Terminal.getInstance();
		Terminal.register(new TestCommand());
		Terminal.register(new RestartCommand());
		Terminal.register(new ExitCommand());
		Terminal.register(new HelpCommand());
		Terminal.register(new DebugCommand());
	}
	
	public static void loadMySQL() {
		MySQL.connect(InstaBase.configuration.getString("mysql.user")
				, InstaBase.configuration.getString("mysql.password")
				, InstaBase.configuration.getString("mysql.host")
				, InstaBase.configuration.getString("mysql.db"));
		//CREATE TABLE
		MySQL.createTable("users"
				, "user_id INT AUTO_INCREMENT PRIMARY KEY"
				+ ", email varchar(255)"
				+ ", password varchar(255)");
	}
	
	public static void loadYML() {
		InstaBase.configuration = YamlConfiguration.loadConfiguration(new File("properties.yml"));
		InstaBase.configuration.setIfNotExist("listenPort", 2000);
		InstaBase.configuration.setIfNotExist("mysql.user", "root");
		InstaBase.configuration.setIfNotExist("mysql.password", "HERE");
		InstaBase.configuration.setIfNotExist("mysql.host", "mysql.statsmc.net");
		InstaBase.configuration.setIfNotExist("mysql.db", "insta");
		InstaBase.configuration.setIfNotExist("debug", false);
		InstaBase.configuration.save();
		
		InstaBase.PORT = InstaBase.configuration.getInt("listenPort");
	}
	
	public static void exit() {
		exit("");
	}
	
	public static void exit(String reason) {
		InstaBase.printf("stopping Server...");
		User.disconnectAll(reason);
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InstaBase.init();

		InstaBase.setPrefix("Server");
		try {
			init();
			InstaBase.printf("InstaServer starts...");
			server = new ServerSocket(InstaBase.PORT);
			InstaBase.printf("Erreichbar unter " + server.getInetAddress().getHostAddress() + ":" + InstaBase.PORT);

			while (!server.isClosed()) {
				try {
					Socket socket = server.accept();
					new User(socket);
					
				}catch(SocketException e) {
					if(!e.getMessage().equalsIgnoreCase("Socket closed"))e.printStackTrace();
				}
			}

			if(!server.isClosed())server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Terminal.getInstance().stop();
		
		System.exit(0);
	}

}
