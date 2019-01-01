package net.insta.server.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import net.insta.base.utils.Utils;
import net.insta.server.connection.User;
import net.insta.server.connection.UserJournal;

public class InstagramUtils {

	public static boolean likeAndComment(User user,Instagram v, String username, String comment) throws InterruptedException {
		try {
			Account account = v.getAccountByUsername(username);

			List<Media> medias = account.getMedia().getNodes();
			if (!medias.isEmpty()) {
				Collections.sort(medias, new Comparator<Media>() {

					@Override
					public int compare(Media o1, Media o2) {
						return o2.getTakenAtTimestamp().compareTo(o1.getTakenAtTimestamp());
					}
				});
				Media media = medias.get(0);
				media = v.getMediaByCode(media.getShortcode());

				int i = Utils.rand(0, 1);
				if (i == 0 && !media.getCommentsDisabled()) {
					if(!UserJournal.checkAction(user, UserJournal.JournalAction.COMMENT, media.getShortcode())) {
						v.addMediaComment(media.getShortcode(), comment);
						UserJournal.addAction(user, UserJournal.JournalAction.COMMENT, media.getShortcode());
						Thread.sleep(Utils.rand(100, 200));
					}
				}
				
				if (!media.getViewerHasLiked()) {
					if(!UserJournal.checkAction(user, UserJournal.JournalAction.LIKE, media.getShortcode())) {
						v.likeMediaByCode(media.getShortcode());
						UserJournal.addAction(user, UserJournal.JournalAction.LIKE, media.getShortcode());
					}
				}
					
				
				if (i == 1 && !media.getCommentsDisabled()) {
					if(!UserJournal.checkAction(user, UserJournal.JournalAction.COMMENT, media.getShortcode())) {
						Thread.sleep(Utils.rand(100, 200));
						v.addMediaComment(media.getShortcode(), comment);
						UserJournal.addAction(user, UserJournal.JournalAction.COMMENT, media.getShortcode());
					}
				}
				

				v.basePage();
				return true;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
}
