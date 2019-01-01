package me.postaddict.instagram.scraper.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "tag_statistics")
@Data
public class Tag {
    @Id
    private String name;
    private Integer count;
    @Transient
    private MediaRating mediaRating;
}
