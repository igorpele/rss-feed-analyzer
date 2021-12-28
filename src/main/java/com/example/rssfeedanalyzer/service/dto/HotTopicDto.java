package com.example.rssfeedanalyzer.service.dto;

import java.util.List;

/**
 * User: pelesic
 */
public class HotTopicDto {

    private String keyword;

    private Integer occurence;

    private List<FeedItemDto> itemsList;

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

    public List<FeedItemDto> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<FeedItemDto> itemsList) {
        this.itemsList = itemsList;
    }
}
