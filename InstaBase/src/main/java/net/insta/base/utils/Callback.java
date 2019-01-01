package net.insta.base.utils;

public interface Callback<T> {
	public void run( T value );
	public default void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
