package net.insta.base.encryption;

public interface ICipher {
	public byte[] decrypt(byte[] encrypted) throws Exception;
	public byte[] encrypt(String message) throws Exception;
	public byte[] encrypt(byte[] message) throws Exception;
}
