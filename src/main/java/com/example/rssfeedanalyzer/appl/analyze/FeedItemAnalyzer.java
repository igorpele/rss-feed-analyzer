package com.example.rssfeedanalyzer.appl.analyze;

import com.example.rssfeedanalyzer.appl.analyze.AnalyzedFeedItem;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.List;

/**
 * User: pelesic
 */
public interface FeedItemAnalyzer {

    AnalyzedFeedItem analyze(SyndEntry entry);

}
