package net.insta.base.packets.client.instagram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;
import net.insta.base.packets.Packet;

@Getter
public class InstagramTestPacket extends Packet{
	private String shortcode;

	public InstagramTestPacket() {}
	
	public InstagramTestPacket(String shortcode) {
		this.shortcode=shortcode;
	}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.shortcode=in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeUTF(shortcode);
	}
}
