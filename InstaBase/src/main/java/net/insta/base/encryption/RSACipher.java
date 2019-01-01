package net.insta.base.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import lombok.Getter;

@Getter
public class RSACipher implements ICipher{
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public RSACipher(PublicKey publicKey, PrivateKey privateKey) {
		this.publicKey=publicKey;
		this.privateKey=privateKey;
	}
	
	public RSACipher(PublicKey publicKey) {
		this(publicKey,null);
	}
	
	public RSACipher() {
		KeyPair pair;
		try {
			pair = buildKey();
			this.publicKey=pair.getPublic();
			this.privateKey=pair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public byte[] encrypt(byte[] bytes) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
		return cipher.doFinal(bytes);
	}
	
	public byte[] encrypt(String message) throws Exception {
		return encrypt(message.getBytes());
	}
	
	public byte[] decrypt(byte[] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
		return cipher.doFinal(encrypted);
	}

	public static KeyPair buildKey() throws NoSuchAlgorithmException {
		final int keySize = 2048;
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keySize);
		return keyPairGenerator.genKeyPair();
	}
}
