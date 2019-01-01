package net.insta.base.packets.response;

import java.util.UUID;

import net.insta.base.packets.Packet;

public interface ResponseListener {
	public void handle(UUID uuid,Packet packet);
}
