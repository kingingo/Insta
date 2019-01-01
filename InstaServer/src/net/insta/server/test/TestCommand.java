package net.insta.server.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageObject;
import net.insta.server.proxy.CustomSocketFactory;
import net.insta.server.terminal.CommandExecutor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class TestCommand implements CommandExecutor {

	public static void main(String[] args) {
		 Instagram instagram = null;
		try {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
	        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

			Proxy proxyTest = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost",2000));
	        OkHttpClient httpClient = new OkHttpClient.Builder()//.socketFactory(new Socks4SocketFactory("localhost", 2000))
	        		.addNetworkInterceptor(loggingInterceptor)
	        		.addInterceptor(new ErrorInterceptor())
					.cookieJar(new DefaultCookieJar(new CookieHashSet()))
					.proxy(proxyTest)
					.build();
	        instagram = new Instagram(httpClient);
			System.out.println("login in...");
	        instagram.basePage();
			System.out.println("login in...1");
	        instagram.login("feliixistda","baker97");
	        instagram.basePage();
			System.out.println("logged in");
			
		
			
		}catch(Exception e) {
			e.printStackTrace();
			
			 try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 try {
				
				instagram.basePage();
				System.out.println("again login in...1");
		        instagram.login("feliixistda","baker97");
		        instagram.basePage();
				System.out.println("again logged in");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				
		}
		
//		try {
//			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//	        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//	        OkHttpClient httpClient = new OkHttpClient.Builder().socketFactory(new CustomTestSocketFactory())
//	        		.addNetworkInterceptor(loggingInterceptor).addInterceptor(new ErrorInterceptor())
//					.cookieJar(new DefaultCookieJar(new CookieHashSet())).build();
//	        Instagram instagram = new Instagram(httpClient);
//			System.out.println("login in...");
//	        instagram.basePage();
//			System.out.println("login in...1");
//	        instagram.login("feliixistda","baker97");
//	        instagram.basePage();
//			System.out.println("logged in");
//			
//
//			System.out.println("load account feliixistda");
//			Account account1 = instagram.getAccountByUsername("feliixistda");
//
//			System.out.println("load account felixoben");
//			Account account = instagram.getAccountByUsername("felixoben");
//
//			System.out.println("load medias");
//			List<Media> medias = account.getMedia().getNodes();
//			if (!medias.isEmpty()) {
//				System.out.println("start sort");
//				Collections.sort(medias, new Comparator<Media>() {
//
//					@Override
//					public int compare(Media o1, Media o2) {
//						return o2.getTakenAtTimestamp().compareTo(o1.getTakenAtTimestamp());
//					}
//				});
//				Media media = medias.get(0);
//				media = instagram.getMediaByCode(media.getShortcode());
//				System.out.println("Found media "+media.getId());
//				System.out.println("Likes: "+media.getLikeCount());
//				System.out.println("Comments: "+media.getCommentCount());
//				System.out.println("isPrivate: "+account.getIsPrivate());
//				
//				boolean follow=false;
//				PageObject<Account> followers = instagram.getFollowers(account.getId(), 1);
//				
//				for(Account a : followers.getNodes()) {
//					if(a.getId() == account1.getId()) {
//						follow=true;
//						break;
//					}
//				}
//				
//				System.out.println("feliixistda followed felixoben: "+follow);
//				if(follow) {
//					instagram.addMediaComment(media.getShortcode(), "Test1");
//
//					if (!media.getViewerHasLiked())
//						instagram.likeMediaByCode(media.getShortcode());
//				}
//				
//				instagram.basePage();
//			}else {
//				System.out.println("isEmpty");
//			}
//			System.out.println("END");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void onCommand(String[] args) {
		
	}

	@Override
	public String getDescription() {
		return "Test Command";
	}

}
