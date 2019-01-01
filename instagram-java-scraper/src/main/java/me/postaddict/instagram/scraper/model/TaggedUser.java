package me.postaddict.instagram.scraper.model;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class TaggedUser {
    private String username;
    private Float x;
    private Float y;
}
