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
public class LoginAnswerPacket extends Packet{
	private String message = "";
	private int userId;
	private boolean success = false;
	
	public LoginAnswerPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.message = in.readUTF();
		this.userId = in.readInt();
		this.success = in.readBoolean();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(message);
		out.writeInt(userId);
		out.writeBoolean(this.success);
	}
}

