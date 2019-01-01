package me.postaddict.instagram.scraper;

import java.io.IOException;

import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Comment;
import me.postaddict.instagram.scraper.model.Location;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;

public interface AnonymousInsta extends StatelessInsta {

    Location getLocationMediasById(String locationId, int pageCount) throws IOException;

    Tag getMediasByTag(String tag, int pageCount) throws IOException;

    PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException;

    Account getAccountById(long id) throws IOException;

}
