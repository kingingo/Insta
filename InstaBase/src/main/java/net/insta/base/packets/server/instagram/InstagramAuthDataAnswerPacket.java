package net.insta.base.packets.server.instagram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@Getter
@AllArgsConstructor
public class InstagramAuthDataAnswerPacket extends Packet{
	private boolean success;
	private String message;
	
	public InstagramAuthDataAnswerPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.success=in.readBoolean();
		this.message=in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeBoolean(this.success);
		out.writeUTF(this.message);
	}

}
