package com.example.rssfeedanalyzer.appl.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: pelesic
 */
@Entity
@Table(name = "HOT_TOPIC")
public class HotTopic extends EntityBase{

    @ManyToOne
    private AnalysisResult analysisResult;

    @Column
    private String keyword;

    @Column
    private Integer occurence;

    @OneToMany(targetEntity = FeedItem.class, mappedBy = "hotTopic", cascade = CascadeType.ALL)
    private List<FeedItem> itemsList = new ArrayList<>();

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    public List<FeedItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<FeedItem> itemsList) {
        this.itemsList = itemsList;
    }


    public HotTopic withAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
        return this;
    }

    public HotTopic withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public HotTopic withOccurence(Integer occurence) {
        this.occurence = occurence;
        return this;
    }

    public HotTopic withItemsList(List<FeedItem> itemsList) {
        this.itemsList = itemsList;
        return this;
    }



}
