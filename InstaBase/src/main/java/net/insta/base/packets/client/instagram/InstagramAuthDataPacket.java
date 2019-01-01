package net.insta.base.packets.client.instagram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;
import net.insta.base.encryption.AuthData;
import net.insta.base.packets.Packet;

@Getter
public class InstagramAuthDataPacket extends Packet{
	private AuthData authData;

	public InstagramAuthDataPacket() {}
	
	public InstagramAuthDataPacket(String username, String password) {
		this.authData=new AuthData(username, password);
	}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.authData = new AuthData();
		this.authData.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		authData.writeToOutput(out);
	}
}
