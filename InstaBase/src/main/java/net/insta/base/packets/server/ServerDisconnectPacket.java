package net.insta.base.packets.server;

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
public class ServerDisconnectPacket extends Packet{
	private String reason="";
	
	public ServerDisconnectPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.reason=in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(this.reason);
	}
}
