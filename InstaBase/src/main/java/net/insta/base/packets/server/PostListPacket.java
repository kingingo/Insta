package net.insta.base.packets.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;
import net.insta.base.post.InstagramPost;

@Getter
@AllArgsConstructor
public class PostListPacket extends Packet{

	private ArrayList<InstagramPost> posts;
	
	public PostListPacket() {
		this.posts=new ArrayList<>();
	}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		int length = in.readInt();
		for(int i = 0; i < length ; i++) {
			posts.add(InstagramPost.loadPost(in));
		}
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(this.posts.size());
		for(InstagramPost post : this.posts) {
			post.writeToOutput(out);
		}
	}

}
