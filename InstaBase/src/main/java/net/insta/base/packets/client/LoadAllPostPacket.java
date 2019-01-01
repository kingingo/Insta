package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@Getter
@AllArgsConstructor
public class LoadAllPostPacket extends Packet{

	private int userId;
	
	public LoadAllPostPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.userId=in.readInt();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(this.userId);
	}

}
