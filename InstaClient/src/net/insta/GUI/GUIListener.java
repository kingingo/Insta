package net.insta.GUI;

import javafx.application.Platform;
import net.insta.GUI.user.User;
import net.insta.base.event.EventHandler;
import net.insta.base.event.EventListener;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.PacketReceiveEvent;
import net.insta.base.event.events.client.ServerDisconnectEvent;
import net.insta.base.packets.server.instagram.InstagramLikedPostPacket;

public class GUIListener implements EventListener {
	private final String SIGN = Character.toString((char) 0x2713);

	public GUIListener() {
		EventManager.register(this);
	}

	@EventHandler
	public void disconnect(ServerDisconnectEvent ev) {
		GUIAPI.setActiveLoginButton(false);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (MainGUI.getInstance() != null)
					MainGUI.getInstance().getStage().hide();
				LoginGUI.getInstance().getStage().show();
			}
		});
	}

	// User actions completed
	@EventHandler
	public void receive(PacketReceiveEvent ev) {
		if (ev.getPacket() instanceof InstagramLikedPostPacket) {
			InstagramLikedPostPacket packet = (InstagramLikedPostPacket) ev.getPacket();
			double p = MainGUI.getInstance().progressBar.getProgress();
			for (User user : MainGUI.getInstance().getUserData()) {
				if (user.getInstaUser().equalsIgnoreCase(packet.getUsername())) {
					if (packet.isSuccess()) {
						user.setDone(SIGN);
						System.out.println("Sign setted");
						MainGUI.getInstance().tableUserList.refresh();
						
						p +=MainGUI.getInstance().percent;
						
						MainGUI.getInstance().progressBar.setProgress(p);
						MainGUI.getInstance().progressIndicator.setProgress(p);
					} else {
						user.setDone("failed");
					}
					break;
				}
			}
			
//			if(packet.isDone()) {
//				MainGUI.getInstance().toogleLikeAndComment();
//			}
		}
	}
}
