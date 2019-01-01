package me.postaddict.instagram.scraper.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Activity {
    @Id
    private String id;
    private Double timestamp;
    private Integer type;
    private ActivityType activityType;
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account user;
    @ManyToOne
    @JoinColumn(name="media_id")
    private Media media;
    private String text;
}
