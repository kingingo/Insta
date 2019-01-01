package me.postaddict.instagram.scraper.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    private long id;
    private Boolean hasPublicPage;
    private String name;
    private String slug;
    private Double lat;
    private Double lng;

    private Integer count;
    @Transient
    private MediaRating mediaRating;
}
