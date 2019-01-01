package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.insta.base.packets.Packet;

@Getter
@Setter
@AllArgsConstructor
public class PingPacket extends Packet{
	private long lastPing = 0;
	
	public PingPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.lastPing = in.readLong();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeLong(this.lastPing);
	}
	
	@Override
	public boolean toEncrypt() {
		return false;
	}
}
