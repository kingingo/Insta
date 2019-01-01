package net.insta.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utils {
	private static Random rdm = new Random();
	private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy_hh-mm-ss");

	public static String toDate(long milis) {
		Date date = new Date();
		date.setTime(milis);
		return format.format(date).toString();
	}
	
	public static int rand(int min, int max) {
		return rdm.nextInt((max - min) + 1) + min;
	}
	
	public static String formatMili(long milis) {
		if (milis > TimeSpan.MINUTE) {
			if (milis > TimeSpan.HOUR) {
				if (milis > TimeSpan.DAY) {
					int time = (int) (milis / TimeSpan.DAY);
					if (milis - time * TimeSpan.DAY > 1) {
						return time + " days " + formatMili(milis - time * TimeSpan.DAY);
					}
					return time + " day";
				}

				int time = (int) (milis / TimeSpan.HOUR);
				if (milis - time * TimeSpan.HOUR > 1) {
					return time + "h " + formatMili(milis - time * TimeSpan.HOUR);
				}
				return time + "h";
			}

			int time = (int) (milis / TimeSpan.MINUTE);
			if (milis - time * TimeSpan.MINUTE > 1) {
				return time + "min " + formatMili(milis - time * TimeSpan.MINUTE);
			}
			return time + "min";
		}

		return (int) (milis / TimeSpan.SECOND) + "sec";
	}
}
