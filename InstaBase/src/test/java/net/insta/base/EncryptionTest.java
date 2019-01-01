package net.insta.base;

import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import org.junit.Test;
import net.insta.base.encryption.AESCipher;
import net.insta.base.encryption.RSACipher;

public class EncryptionTest {
	/**
	 * Der AES Key wird über den RSA PubliyKey verschlüsselt (vom Client) und wieder entschlüsselt (Vom Server)
	 * @throws Exception
	 */
	@Test
	public void encryptionTest() throws Exception {
		AESCipher aes = new AESCipher();
		RSACipher rsa = new RSACipher();
		
		byte[] encrypted = rsa.encrypt(aes.getSecret().getEncoded());
		byte[] decrypted = rsa.decrypt(encrypted);
		
		assertTrue(Arrays.equals(decrypted, aes.getSecret().getEncoded()));
	}
}
