package net.insta.server.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketTimeoutException;

public class SocksSocket extends Socket {

	public static SocksSocket create() {
		try {
			Constructor cons = Class.forName("java.net.SocksSocketImpl").getDeclaredConstructor();

			cons.setAccessible(true);
			SocketImpl si = (SocketImpl) cons.newInstance();

			return new SocksSocket(si);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final int PROTO_VERS4 = 4;
	private static final int CONNECT = 1;

	private SocksSocket(SocketImpl s) throws SocketException {
		super(s);
	}

	private int readSocksReply(byte[] data) throws IOException {
		int len = data.length;
		int received = 0;
		for (int attempts = 0; received < len && attempts < 3; attempts++) {
			int count;
			try {
				count = getInputStream().read(data, received, len - received);
			} catch (SocketTimeoutException e) {
				throw new SocketTimeoutException("Connect timed out");
			}
			if (count < 0)
				throw new SocketException("Malformed reply from SOCKS server");
			received += count;
		}
		return received;
	}

	public void connectV4(InetSocketAddress endpoint) throws IOException {
		if (!(endpoint.getAddress() instanceof Inet4Address)) {
			throw new SocketException("SOCKS V4 requires IPv4 only addresses");
		}
		OutputStream out = getOutputStream();
		out.write(PROTO_VERS4);
		out.write(CONNECT);
		out.write((endpoint.getPort() >> 8) & 0xff);
		out.write((endpoint.getPort() >> 0) & 0xff);
		out.write(endpoint.getAddress().getAddress());
		String userName = "";
		try {
			out.write(userName.getBytes("ISO-8859-1"));
		} catch (java.io.UnsupportedEncodingException uee) {
			assert false;
		}
		out.write(0);
		out.flush();
		byte[] data = new byte[8];
		int n = readSocksReply(data);
		if (n != 8)
			throw new SocketException("Reply from SOCKS server has bad length: " + n);
		if (data[0] != 0 && data[0] != 4)
			throw new SocketException("Reply from SOCKS server has bad version");
		SocketException ex = null;
		switch (data[1]) {
		case 90:
			// Success!
			// external_address = endpoint;
//			setExternalAddress(socket, endpoint);
			break;
		case 91:
			ex = new SocketException("SOCKS request rejected");
			break;
		case 92:
			ex = new SocketException("SOCKS server couldn't reach destination");
			break;
		case 93:
			ex = new SocketException("SOCKS authentication failed");
			break;
		default:
			ex = new SocketException("Reply from SOCKS server contains bad status");
			break;
		}
		if (ex != null) {
			getInputStream().close();
			out.close();
			throw ex;
		}
	}

	private void setExternalAddress(Socket socket, InetSocketAddress endpoint) {
		try {
			Field field = Socket.class.getDeclaredField("impl");
			field.setAccessible(true);
			Object impl = field.get(socket);

			Field field1 = Class.forName("java.net.SocksSocketImpl").getDeclaredField("external_address");
			field1.setAccessible(true);
			field1.set(impl, endpoint);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
