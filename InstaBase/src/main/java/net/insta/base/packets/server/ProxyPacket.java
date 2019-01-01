package net.insta.base.packets.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.insta.base.packets.Packet;

@Getter
@Setter
@AllArgsConstructor
public class ProxyPacket extends Packet{
	private int port;
	
	public ProxyPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.port=in.readInt();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(port);
	}
	
	@Override
	public boolean toEncrypt() {
		return true;
	}
}
