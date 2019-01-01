package me.postaddict.instagram.scraper.model;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class DisplayResource {
    private String src;
    private Integer width;
    private Integer height;
}
