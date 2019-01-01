package net.insta.base.encryption;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;

public class AESCipher implements ICipher{
	@Getter
	private SecretKey secret;
	
	public AESCipher() {
		try {
			this.secret=buildKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public AESCipher(SecretKey secret) {
		this.secret=secret;
	}
	
	public static SecretKey buildKey() throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128);
		return generator.generateKey();
	}
	
	public byte[] encrypt(String message) throws Exception {
		return encrypt(message.getBytes());
	}

	public byte[] encrypt(byte[] bytes) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		return cipher.doFinal(bytes);
	}

	public byte[] decrypt(byte[] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secret);
		return cipher.doFinal(encrypted);
	}
	
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(secret.getEncoded().length);
		out.write(secret.getEncoded());
	}
	
	public static SecretKey parseFromInput(DataInputStream in) throws IOException {
		byte[] key = new byte[in.readInt()];
		in.read(key, 0, key.length);
		return new SecretKeySpec(key, "AES");
	}
}
