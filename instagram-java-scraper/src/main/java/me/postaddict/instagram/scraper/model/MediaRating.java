package me.postaddict.instagram.scraper.model;

import java.util.List;

import lombok.Data;

@Data
public class MediaRating {
    private PageObject<Media> media;
    private List<Media> topPosts;
}
