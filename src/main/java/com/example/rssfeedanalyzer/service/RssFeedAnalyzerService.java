package com.example.rssfeedanalyzer.service;

import com.example.rssfeedanalyzer.service.dto.AnalysisResultDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * User: pelesic
 */
public interface RssFeedAnalyzerService {

    CompletableFuture<String> analyzeFeeds(List<String> urls);

    AnalysisResultDto getAnalysisResult(String resultId);
}
