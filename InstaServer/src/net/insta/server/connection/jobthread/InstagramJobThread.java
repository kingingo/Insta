package net.insta.server.connection.jobthread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Setter;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import net.insta.base.client.ClientStatus;
import net.insta.base.encryption.AuthData;
import net.insta.base.packets.server.ResponsePacket;
import net.insta.base.packets.server.instagram.InstagramAuthDataAnswerPacket;
import net.insta.base.utils.Callback;
import net.insta.server.connection.User;
import net.insta.server.proxy.CustomSocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class InstagramJobThread implements Runnable {

	private LinkedBlockingQueue<Callback<Instagram>> queue = new LinkedBlockingQueue<>();
	private User user;
	private boolean active = true;
	private Thread thread;
	private Instagram instagram;
	@Setter
	public AuthData authData;
	public UUID response;

	public InstagramJobThread(User user) {
		this.thread = new Thread(this);
		this.user = user;
	}

	public InstagramJobThread(User user, AuthData authData) {
		this(user);
		this.authData = authData;
	}
	
	public void start(UUID response) {
		this.response=response;
		this.thread.start();
	}

	public boolean login() throws Exception {
		if(instagram == null) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
			Proxy proxyTest = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(user.getAdress(), 8080));
			OkHttpClient httpClient = new OkHttpClient.Builder()
					.socketFactory(CustomSocketFactory.getDefault())
					.proxy(proxyTest)
					.addNetworkInterceptor(loggingInterceptor)
					.addInterceptor(new ErrorInterceptor())
	                .addInterceptor(new UserAgentInterceptor(UserAgents.ANDROID_7_0))
					.cookieJar(new DefaultCookieJar(new CookieHashSet()))
					.build();
			
			this.instagram = new Instagram(httpClient);
		}
		
		if (authData != null) {
			String username = authData.getDecryptedUsername();
			String password = authData.getDecryptedPassword();
			
			this.instagram.basePage();
			this.instagram.login(username, password);
			this.instagram.basePage();
			this.active = true;
			return true;
		}
		
		return false;
	}

	public void logout() {
		this.active = false;
		this.queue.clear();
		this.thread.stop();
//		this.thread=new Thread(this);
	}

	public void add(Callback<Instagram> callback) {
		queue.add(callback);
	}

	@Override
	public void run() {
		if(response!=null) {
			user.printf("try to login");
			String message="";
			try {
				active=login();
				user.setStatus(ClientStatus.AUTHENTICATED);
				message="login sucessfully";
				user.printf("logged in to instagram");
			} catch (InstagramAuthException e) {
				active=false;
				user.setStatus(ClientStatus.LOGGEDIN);
				message="login failt "+e.getMessage();
				user.printf("login failt to instagram " +e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				user.setStatus(ClientStatus.LOGGEDIN);
				active=false;
				message="login failt please contact an administrator! "+e.getMessage();
				user.printf("login failt to instagram");
			}
			user.write(new ResponsePacket(response,new InstagramAuthDataAnswerPacket(active,message)));
		}

		while (active) {
			if (!queue.isEmpty()) {
				try {
					Callback<Instagram> callback = queue.poll();
					callback.run(instagram);
				}catch(Exception e) {
					e.printStackTrace();
					user.printf("something went wrong InstagramJobThread...");
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		logout();
	}

}
