package net.insta.base.packets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.encryption.RSACipher;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class AESKeyPacket extends Packet {
	private byte[] key;

	public AESKeyPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		key = new byte[in.readInt()];
		in.read(key, 0, key.length);
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(key.length);
		out.write(key);
	}
	
	@Override
	public boolean toEncrypt() {
		return false;
	}

	public static SecretKey toSecretKey(byte[] key, RSACipher cipher) throws NullPointerException{
		try {
			key = cipher.decrypt(key);
			return new SecretKeySpec(key, "AES");
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("Key couldn't resolve");
		}
	}
}
