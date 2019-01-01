package net.insta.base.post;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.insta.base.client.Client;
import net.insta.base.packets.client.LoadPostPacket;
import net.insta.base.packets.response.Response;
import net.insta.base.packets.server.PostPacket;

public class InstagramPost {
	private byte[] bytes;
	private UUID uuid;
	private int userId;
	private String description;
	private long postDate;
	
	private InstagramPost() {}
	
	public void save() {
		saveLocal();
		saveExternal();
	}
	
	private void saveExternal() {
		PostPacket packet = new PostPacket(this, true);
		Client.get(userId).write(packet);
	}
	
	private void saveLocal() {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(getLocalFile(this)));
			writeToOutput(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private boolean loadLocal() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(getLocalFile(this)));
			parseFromInput(in);
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find File "+getLocalFile(this).getAbsolutePath()+" for UUID:"+uuid.toString()+" userId:"+this.userId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void parseFromInput(DataInputStream in) throws IOException {
		this.userId=in.readInt();
		this.uuid=UUID.fromString(in.readUTF());
		this.description=in.readUTF();
		this.postDate=in.readLong();
		int length = in.readInt();
		byte[] bytes = new byte[length];
		in.read(bytes, 0, length);
	}
	
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(this.userId);
		out.writeUTF(this.uuid.toString());
		out.writeUTF(this.description);
		out.writeLong(this.postDate);
		out.writeInt(bytes.length);
		out.write(bytes, 0, bytes.length);
	}
	
	public static InstagramPost loadPost(DataInputStream in) {
		try {
			InstagramPost post = new InstagramPost();
			post.parseFromInput(in);
			return post;
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new NullPointerException();
	}
	
	private static File getLocalFile(InstagramPost post) {
		return getLocalFile(post.userId, post.uuid);
	}
	
	private static File getLocalFile(int userId, UUID uuid) {
		return new File("posts/"+userId+"/"+uuid.toString()+".post");
	}
	
	private static InstagramPost loadExternal(int userId, UUID uuid) {
		Response<PostPacket> response = Client.get(userId).writeRequest(new LoadPostPacket(uuid,userId));
		PostPacket packet = response.getSync();
		
		if(packet.isFound()) {
			return packet.getPost();
		}
		return null;
	}
	
	private static boolean isLocal(int userId, UUID uuid) {
		return getLocalFile(userId,uuid).exists();
	}
	
	public static InstagramPost loadPost(int userId, UUID uuid) throws Exception {
		InstagramPost post;
		if(isLocal(userId, uuid)) {
			post = new InstagramPost();
			post.loadLocal();
			return post;
		}else if((post = loadExternal(userId,uuid))!=null) {
			post.saveLocal();
			return post;
		}
		throw new NullPointerException();
	}
}
