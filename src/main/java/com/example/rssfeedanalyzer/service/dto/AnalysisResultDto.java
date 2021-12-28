package com.example.rssfeedanalyzer.service.dto;

import java.util.List;

/**
 * User: pelesic
 */
public class AnalysisResultDto {

    private String resultId;
    private List<HotTopicDto> hotTopicList;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public List<HotTopicDto> getHotTopicList() {
        return hotTopicList;
    }

    public void setHotTopicList(List<HotTopicDto> hotTopicList) {
        this.hotTopicList = hotTopicList;
    }
}
