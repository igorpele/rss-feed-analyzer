package com.example.rssfeedanalyzer.appl.retrieve;

import com.rometools.rome.feed.synd.SyndFeed;

/**
 * User: pelesic
 */
public interface RssFeedRetriever {

    SyndFeed retrieveFeed(String url) throws RssFeedRetrievingException;

}
