package me.postaddict.instagram.scraper.model;

import java.util.List;

import lombok.Data;

@Data
public class ActivityFeed {
    private String timestamp;
    private Integer count;
    private List<Activity> activities;
}
