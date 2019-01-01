package net.insta.base.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.client.Client;
import net.insta.base.event.Event;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class PacketReceiveEvent extends Event{
	private Client client;
	private Packet packet;
}
