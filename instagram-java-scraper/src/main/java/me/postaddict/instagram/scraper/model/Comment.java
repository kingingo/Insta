package me.postaddict.instagram.scraper.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    private Long id;
    @Column(name = "text", length = 4096)
    private String text;
    private Long createdAt;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Account owner;

    @Transient
    public Date getCreated(){
        return createdAt != null ? new Date(createdAt) : null;
    }
}
