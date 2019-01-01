package net.insta.base.packets.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import lombok.Getter;
import net.insta.base.packets.Packet;

@Getter
public class ResponsePacket extends Packet{
	private UUID uuid;
	private Packet packet;

	public ResponsePacket() {}
	
	public ResponsePacket(UUID uuid, Packet packet) {
		this.packet=packet;
		this.uuid=uuid;
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
