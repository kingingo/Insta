package net.insta.server.connection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKey;

import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.model.Media;
import net.insta.base.InstaBase;
import net.insta.base.client.ClientStatus;
import net.insta.base.encryption.AESCipher;
import net.insta.base.encryption.RSACipher;
import net.insta.base.event.EventHandler;
import net.insta.base.event.EventListener;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.packets.client.AESKeyPacket;
import net.insta.base.packets.client.HandshakePacket;
import net.insta.base.packets.client.LoginPacket;
import net.insta.base.packets.client.PingPacket;
import net.insta.base.packets.client.RequestPacket;
import net.insta.base.packets.client.instagram.InstagramAuthDataPacket;
import net.insta.base.packets.client.instagram.InstagramLikeLastPostPacket;
import net.insta.base.packets.client.instagram.InstagramTestPacket;
import net.insta.base.packets.server.HandshakeAnswerPacket;
import net.insta.base.packets.server.LoginAnswerPacket;
import net.insta.base.packets.server.PongPacket;
import net.insta.base.packets.server.RSAPublicKeyPacket;
import net.insta.base.packets.server.ResponsePacket;
import net.insta.base.packets.server.instagram.InstagramLikedPostPacket;
import net.insta.base.packets.server.instagram.InstagramTestAnswerPacket;
import net.insta.base.utils.Callback;
import net.insta.base.utils.Utils;
import net.insta.server.mysql.MySQL;
import net.insta.server.utils.InstagramUtils;

public class UserListener implements EventListener {

	public UserListener() {
		EventManager.register(this);
	}

	@EventHandler
	public void ping(PacketReceiveEvent ev) {
		if (ev.getPacket() instanceof PingPacket) {
			User user = (User) ev.getClient();
			user.write(new PongPacket());
			user.setLastPing(System.currentTimeMillis());
			user.setPing(((PingPacket) ev.getPacket()).getLastPing());
		}
	}

	@EventHandler
	public void handshake(PacketReceiveEvent ev) {
		User user = (User) ev.getClient();

		if (user.getStatus() == ClientStatus.NOT_AUTHENTICATED && ev.getPacket() instanceof HandshakePacket) {
			HandshakePacket packet = (HandshakePacket) ev.getPacket();

			if (!InstaBase.VERSION.equalsIgnoreCase(packet.getVERSION())) {
				user.write(new HandshakeAnswerPacket(InstaBase.VERSION + " != " + packet.getVERSION(), false));
			} else {
				user.setStatus(ClientStatus.HANDSHAKED);
				user.write(new HandshakeAnswerPacket("Software is up to date", true));
				
				user.setCipher(new RSACipher());
				user.write(new RSAPublicKeyPacket( ((RSACipher)user.getCipher()).getPublicKey())); // AFTER HANDSHAKE !!
			}
		}
	}

	@EventHandler
	public void login(PacketReceiveEvent ev) {
		User user = (User) ev.getClient();

		if (user.getStatus() == ClientStatus.HANDSHAKED) {
			if (ev.getPacket() instanceof AESKeyPacket) {
				AESKeyPacket packet = (AESKeyPacket) ev.getPacket();
				SecretKey secret = AESKeyPacket.toSecretKey(packet.getKey(), ((RSACipher) user.getCipher()));
				user.setCipher(new AESCipher(secret));
			} else if (ev.getPacket() instanceof RequestPacket) {
				RequestPacket request = (RequestPacket) ev.getPacket();

				if (request.getPacket() instanceof LoginPacket) {
					LoginPacket packet = (LoginPacket) request.getPacket();

					MySQL.asyncQuery("SELECT user_id, password FROM users WHERE email='" + packet.getMail() + "';",
							new Callback<ResultSet>() {

								@Override
								public void run(ResultSet value) {
									try {
										LoginAnswerPacket answer = null;
										while (value.next()) {
											String password = value.getString("password");
											int user_id = value.getInt("user_id");

											if (password.equals(packet.getPassword())) {
												user.setStatus(ClientStatus.HANDSHAKED);
												user.setID(user_id);
												user.setEmail(packet.getMail());
												user.printf("Handshake successfull");
												answer = new LoginAnswerPacket("login succesfull",user_id, true);
											} else {
												user.printf("Handshake failed: wrong password");
												answer = new LoginAnswerPacket("wrong username or password",user_id, false);
											}

										}

										if (answer == null) {
											user.printf("Handshake failed: wrong username");
											answer = new LoginAnswerPacket("wrong username or password",0, false);
										}

										if (answer.isSuccess())
											user.setStatus(ClientStatus.LOGGEDIN);

										user.write(new ResponsePacket(request.getUuid(), answer));
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							});
				}
			}
		}
	}

	@EventHandler
	public void receivePacketRequest(PacketReceiveEvent ev) {
		User user = (User) ev.getClient();

		if (user.getStatus() == ClientStatus.LOGGEDIN) {
			if (ev.getPacket() instanceof RequestPacket) {
				RequestPacket request = (RequestPacket) ev.getPacket();

				if (request.getPacket() instanceof InstagramAuthDataPacket) {
					InstagramAuthDataPacket packet = (InstagramAuthDataPacket) request.getPacket();
					user.getJobThread().setAuthData(packet.getAuthData());
					user.getJobThread().start(request.getUuid());
				}
			}
		}
	}
	
	@EventHandler
	public void test(PacketReceiveEvent ev) {
		User user = (User) ev.getClient();
		
		if(user.getStatus() == ClientStatus.AUTHENTICATED && ev.getPacket() instanceof RequestPacket) {
			RequestPacket request = (RequestPacket)ev.getPacket();
			
			if(request.getPacket() instanceof InstagramTestPacket) {
				InstagramTestPacket packet = (InstagramTestPacket)request.getPacket();
				
				user.getJobThread().add(new Callback<Instagram>() {

					@Override
					public void run(Instagram v) {
						try {
							Media media = v.getMediaByCode(packet.getShortcode());
							user.write(new ResponsePacket(request.getUuid(), new InstagramTestAnswerPacket(media.getOwner().getUsername())));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				});
			}
		}
	}

	@EventHandler
	public void receive(PacketReceiveEvent ev) {
		User user = (User) ev.getClient();

		if (user.getStatus() == ClientStatus.AUTHENTICATED) {
			if (ev.getPacket() instanceof InstagramLikeLastPostPacket) {
				InstagramLikeLastPostPacket packet = (InstagramLikeLastPostPacket) ev.getPacket();
				int length = packet.getComments().size() - 1;

				user.getJobThread().add(new Callback<Instagram>() {

					@Override
					public void run(Instagram v) {
						boolean success;
						for (String username : packet.getAccounts()) {
							if (packet.isSimulation()) {
								user.write(new InstagramLikedPostPacket(username, true,(username.equals(packet.getAccounts().get(packet.getAccounts().size()-1)) ? true : false)));
								sleep(Utils.rand(25, 50));
								continue;
							}
							
							try {
								success = InstagramUtils.likeAndComment(user,v, username,
										packet.getComments().get(Utils.rand(0, length)));
								
								user.printf("continue "+success+" "+username);
								user.write(new InstagramLikedPostPacket(username, success,(username.equals(packet.getAccounts().get(packet.getAccounts().size()-1)) ? true : false) )); // Es wird gepr�ft ob es der letzte Account war.
								sleep(Utils.rand(100, 150));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		}
	}

}
