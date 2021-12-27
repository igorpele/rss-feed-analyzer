package com.example.rssfeedanalyzer.appl.analyze;

import com.example.rssfeedanalyzer.appl.domain.AnalysisResult;

import java.util.List;

/**
 * User: pelesic
 */
public interface FeedCollectionAnalyzer {

    AnalysisResult analyzeFeeds(List<String> urls);

}
