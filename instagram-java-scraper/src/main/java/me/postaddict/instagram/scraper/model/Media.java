package me.postaddict.instagram.scraper.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class Media {
    protected Integer height;
    protected Integer width;
    protected String displayUrl;
    protected String videoUrl;
    @ElementCollection
    protected List<DisplayResource> displayResources;
    protected Boolean isVideo;
    @Transient
    protected Boolean shouldLogClientEvent;
    @Transient
    protected String trackingToken;
    private MediaType mediaType;
    @Id
    private long id;
    private String shortcode;
    @Transient
    private String gatingInfo;
    @Column(name = "caption", length = 4096)
    private String caption;
    private Integer commentCount;
    @Transient
    private PageObject<Comment> commentPreview;
    @ManyToMany
    private List<Comment> firstComments;
    private Boolean commentsDisabled;
    private Boolean captionIsEdited;
    private Long takenAtTimestamp;
    private Integer likeCount;
    private Integer videoViewCount;
    @ManyToMany
    private List<Account> firstLikes;
    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Account owner;
    private Boolean viewerHasLiked;
    private Boolean viewerHasSaved;
    private Boolean viewerHasSavedToCollection;
    private Boolean isAdvertising;
    @OneToMany(mappedBy = "parentMedia")
    private Collection<CarouselResource> carouselMedia;
    @ElementCollection
    private Collection<TaggedUser> taggedUser;
    private Date lastUpdated = new Date();

    @Transient
    public Date getCreated(){
        return takenAtTimestamp != null ? new Date(takenAtTimestamp) : null;
    }
}
