package net.insta.base.packets.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;
import net.insta.base.post.InstagramPost;

@AllArgsConstructor
@Getter
public class PostPacket extends Packet{

	private InstagramPost post;
	private boolean found;
	
	public PostPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		this.found=in.readBoolean();
		if(this.found)this.post=InstagramPost.loadPost(in);
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeBoolean(this.found);
		if(this.found)this.post.writeToOutput(out);
	}

}
