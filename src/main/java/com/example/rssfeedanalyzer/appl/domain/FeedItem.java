package com.example.rssfeedanalyzer.appl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: pelesic
 */
@Entity
@Table(name = "FEED_ITEM")
public class FeedItem extends EntityBase {

    @ManyToOne
    private HotTopic hotTopic;

    @Column(length = 4096)
    private String title;

    @Column(length = 2048)
    private String link;

    public HotTopic getHotTopic() {
        return hotTopic;
    }

    public void setHotTopic(HotTopic hotTopic) {
        this.hotTopic = hotTopic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public FeedItem withHotTopic(HotTopic hotTopic) {
        this.hotTopic = hotTopic;
        return this;
    }

    public FeedItem withTitle(String title) {
        this.title = title;
        return this;
    }

    public FeedItem withLink(String link) {
        this.link = link;
        return this;
    }
}
