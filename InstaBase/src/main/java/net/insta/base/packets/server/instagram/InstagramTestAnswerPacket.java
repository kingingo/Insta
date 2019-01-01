package net.insta.base.packets.server.instagram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class InstagramTestAnswerPacket extends Packet{
	private String username;
	
	 public InstagramTestAnswerPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.username=in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(username);
	}

}
