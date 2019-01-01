package net.insta.base.packets.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.packets.Packet;

@Getter
@Setter
public class PongPacket extends Packet{

	public PongPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		
	}
	
	@Override
	public boolean toEncrypt() {
		return false;
	}
}
