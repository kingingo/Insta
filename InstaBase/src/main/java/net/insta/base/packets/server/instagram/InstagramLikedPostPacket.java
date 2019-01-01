package net.insta.base.packets.server.instagram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class InstagramLikedPostPacket extends Packet{
	private String username;
	private boolean success;
	private boolean done;
	
	 public InstagramLikedPostPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.username=in.readUTF();
		this.success=in.readBoolean();
		this.done=in.readBoolean();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeBoolean(success);
		out.writeBoolean(done);
	}

}
