package me.postaddict.instagram.scraper;

import java.io.IOException;

import me.postaddict.instagram.scraper.model.Account;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageInfo;
import me.postaddict.instagram.scraper.model.PageObject;
import me.postaddict.instagram.scraper.model.Tag;

public interface StatelessInsta {

    void basePage() throws IOException;
    Account getAccountByUsername(String username) throws IOException;
    PageObject<Media> getMedias(String username, int pageCount) throws IOException;
    PageObject<Media> getMedias(long userId, int pageCount, PageInfo pageCursor) throws IOException;
    Media getMediaByUrl(String url) throws IOException;
    Media getMediaByCode(String code) throws IOException;
    Tag getTagByName(String tagName) throws IOException;

}
