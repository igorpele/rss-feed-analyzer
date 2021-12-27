package com.example.rssfeedanalyzer.appl.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: pelesic
 */
@Entity
@Table(name = "ANALYSIS_RESULT", indexes = @Index(columnList = "resultId"))
public class AnalysisResult extends EntityBase {

    @Column
    private String resultId;

    @OneToMany(targetEntity = HotTopic.class, mappedBy = "analysisResult", cascade = CascadeType.ALL)
    @OrderBy("occurence DESC")
    private List<HotTopic> hotTopicList = new ArrayList<>();


    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public List<HotTopic> getHotTopicList() {
        return hotTopicList;
    }

    public void setHotTopicList(List<HotTopic> hotTopicList) {
        this.hotTopicList = hotTopicList;
    }

    public AnalysisResult withResultId(String resultId) {
        setResultId(resultId);
        return this;
    }

    public AnalysisResult withHotTopicList(List<HotTopic> hotTopicList) {
        setHotTopicList(hotTopicList);
        return this;
    }
}
