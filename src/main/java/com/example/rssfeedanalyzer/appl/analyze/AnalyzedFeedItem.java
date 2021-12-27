package com.example.rssfeedanalyzer.appl.analyze;

import java.util.ArrayList;
import java.util.List;

/**
 * User: pelesic
 */
public class AnalyzedFeedItem {

    private String title;
    private String link;
    private List<String> keyWords;

    public AnalyzedFeedItem(String title, String link, List<String> keywords) {
        this.title = title;
        this.link = link;
        this.keyWords = keywords;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalyzedFeedItem that = (AnalyzedFeedItem) o;

        if (!title.equals(that.title)) return false;
        return link.equals(that.link);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + link.hashCode();
        return result;
    }
}
