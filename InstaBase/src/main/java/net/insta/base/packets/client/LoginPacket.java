package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import net.insta.base.packets.Packet;

@Getter
@Setter
public class LoginPacket extends Packet{
	private String mail;
	private String password;

	public LoginPacket() {}
	
	public LoginPacket(String mail, String password) {
		this.mail=mail;
		this.password=password;
	}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.mail=in.readUTF();
		this.password=in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(this.mail);
		out.writeUTF(this.password);
	}
}
