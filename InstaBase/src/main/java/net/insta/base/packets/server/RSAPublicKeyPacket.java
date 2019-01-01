package net.insta.base.packets.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.packets.Packet;

@AllArgsConstructor
@Getter
public class RSAPublicKeyPacket extends Packet {
	private PublicKey publicKey;

	public RSAPublicKeyPacket() {}
	
	@Override
	public void parseFromInput(DataInputStream in) throws IOException {
		int length = in.readInt();
		byte[] servPubKeyBytes = new byte[length];
		in.read(servPubKeyBytes, 0, length);
		X509EncodedKeySpec ks = new X509EncodedKeySpec(servPubKeyBytes);
		
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publicKey = kf.generatePublic(ks);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeToOutput(DataOutputStream out) throws IOException {
		out.writeInt(publicKey.getEncoded().length);
		out.write(publicKey.getEncoded());
	}
	
	@Override
	public boolean toEncrypt() {
		return false;
	}
}
