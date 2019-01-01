package me.postaddict.instagram.scraper.model;

import java.util.List;

import lombok.Data;

@Data
public class PageObject<T> {

    private List<T> nodes;
    private Integer count;
    private PageInfo pageInfo;
}
