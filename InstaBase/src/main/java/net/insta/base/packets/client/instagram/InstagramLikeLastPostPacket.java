package net.insta.base.packets.client.instagram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@Getter
@AllArgsConstructor
public class InstagramLikeLastPostPacket extends Packet{
	private boolean simulation;
	private List<String> accounts;
	private List<String> comments;
	
	public InstagramLikeLastPostPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.simulation=in.readBoolean();
		this.accounts=new ArrayList<>();
		this.comments=new ArrayList<>();
		
		int size = in.readInt();
		for(int i = 0; i < size; i++)accounts.add(in.readUTF());
		size = in.readInt();
		for(int i = 0; i < size; i++)comments.add(in.readUTF());
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeBoolean(this.simulation);
		out.writeInt(accounts.size());
		for(String value : accounts)out.writeUTF(value);
		
		out.writeInt(comments.size());
		for(String value : comments)out.writeUTF(value);
	}
}
