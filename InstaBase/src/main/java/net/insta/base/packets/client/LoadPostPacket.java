package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class LoadPostPacket extends Packet{

	private UUID uuid;
	private int userId;
	
	public LoadPostPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.userId=in.readInt();
		this.uuid=UUID.fromString(in.readUTF());
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(this.userId);
		out.writeUTF(this.uuid.toString());
	}

}
