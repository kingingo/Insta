package net.insta.base.encryption;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;

public class AuthData {
	@Getter
	private byte[] username;
	@Getter
	private byte[] password;
	@Getter
	private AESCipher AES;
	
	public AuthData() {}
	
	public AuthData(String username, String password) {
		this.AES = new AESCipher();
		try {
			this.username = this.AES.encrypt(username);
			this.password = this.AES.encrypt(password);
//			this.username=username.getBytes();
//			this.password=password.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDecryptedUsername() throws Exception {
		return new String(this.AES.decrypt(this.username));
//		return new String(this.username);
	}
	
	public String getDecryptedPassword() throws Exception {
		return new String(this.AES.decrypt(this.password));
//		return new String(this.password);
	}
	
	public void parseFromInput(DataInputStream in) throws IOException {
		this.AES = new AESCipher(AESCipher.parseFromInput(in));
		
		this.username = new byte[in.readInt()];
		in.read(this.username, 0, username.length);

		this.password = new byte[in.readInt()];
		in.read(this.password, 0, password.length);
	}

	public void writeToOutput(DataOutputStream out) throws IOException {
		this.AES.writeToOutput(out);
		
		out.writeInt(this.username.length);
		out.write(this.username);
		
		out.writeInt(this.password.length);
		out.write(this.password);
	}
}
