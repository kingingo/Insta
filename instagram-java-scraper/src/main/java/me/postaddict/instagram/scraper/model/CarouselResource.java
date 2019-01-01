package me.postaddict.instagram.scraper.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class CarouselResource{
    @Id
    private String shortcode;
    @ManyToOne
    @JoinColumn(name="parent_media_id")
    private Media parentMedia;

    protected Integer height;
    protected Integer width;
    protected String displayUrl;
    protected String videoUrl;
    @ElementCollection
    protected List<DisplayResource> displayResources;
    protected Boolean isVideo;
    private Integer videoViewCount;
    @Transient
    protected Boolean shouldLogClientEvent;
    @Transient
    protected String trackingToken;
    @ElementCollection
    protected Collection<TaggedUser> taggedUser;
}
