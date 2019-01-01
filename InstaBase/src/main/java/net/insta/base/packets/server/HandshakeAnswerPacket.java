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
public class HandshakeAnswerPacket extends Packet{
	private String message = "";
	private boolean success = false;
	
	public HandshakeAnswerPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.message = in.readUTF();
		this.success = in.readBoolean();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(message);
		out.writeBoolean(this.success);
	}
	
	@Override
	public boolean toEncrypt() {
		return false;
	}
}

