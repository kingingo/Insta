package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.InstaBase;
import net.insta.base.packets.Packet;

@Getter
@Setter
public class HandshakePacket extends Packet{
	private String VERSION = InstaBase.VERSION;
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.VERSION = in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(this.VERSION);
	}
	
	@Override
	public boolean toEncrypt() {
		return false;
	}
}
