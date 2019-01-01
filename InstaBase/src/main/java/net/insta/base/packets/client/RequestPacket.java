package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import lombok.Getter;
import net.insta.base.packets.Packet;

public class RequestPacket extends Packet{
	@Getter
	private UUID uuid;
	@Getter
	private Packet packet;
	
	public RequestPacket() {}
	
	public RequestPacket(Packet packet) {
		this.packet=packet;
		this.uuid=UUID.randomUUID();
	}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.uuid = UUID.fromString(in.readUTF());

		int packetLength = in.readInt();
		byte packetId=in.readByte();
		byte[] packetBytes = new byte[packetLength];
		in.readFully(packetBytes, 0, packetLength);
		this.packet=Packet.create(packetId, packetBytes);
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(uuid.toString());
		byte[] packetBytes = packet.toByteArray();
		out.writeInt(packetBytes.length);
		out.writeByte(packet.getId());
		out.write(packetBytes);
	}

}
