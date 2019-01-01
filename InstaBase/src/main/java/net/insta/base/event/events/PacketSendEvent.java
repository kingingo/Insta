package net.insta.base.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.insta.base.client.Client;
import net.insta.base.event.Cancellable;
import net.insta.base.event.Event;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class PacketSendEvent  extends Event implements Cancellable{
	@Setter
	private boolean cancelled = false;
	private Client client;
	private Packet packet;
}
