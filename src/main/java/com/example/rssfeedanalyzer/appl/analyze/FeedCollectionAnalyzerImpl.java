package com.example.rssfeedanalyzer.appl.analyze;

import com.example.rssfeedanalyzer.RssFeedAnalyzerException;
import com.example.rssfeedanalyzer.appl.domain.AnalysisResult;
import com.example.rssfeedanalyzer.appl.domain.FeedItem;
import com.example.rssfeedanalyzer.appl.domain.HotTopic;
import com.example.rssfeedanalyzer.appl.retrieve.RssFeedRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: pelesic
 */
@Component
public class FeedCollectionAnalyzerImpl implements FeedCollectionAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedCollectionAnalyzerImpl.class);

    @Value("${relevantHotTopicsCount}")
    private int relevantHotTopics;

    private RssFeedRetriever rssFeedRetriever;
    private FeedItemAnalyzer feedItemAnalyzer;

    @Autowired
    public FeedCollectionAnalyzerImpl(RssFeedRetriever rssFeedRetriever, FeedItemAnalyzer feedItemAnalyzer) {
        this.rssFeedRetriever = rssFeedRetriever;
        this.feedItemAnalyzer = feedItemAnalyzer;
    }

    @Override
    public AnalysisResult analyzeFeeds(List<String> urls) {

        try {
            List<AnalyzedFeedItem> analyzedItems = urls.parallelStream()
                    .map(rssFeedRetriever::retrieveFeed)
                    .map(f -> f.getEntries())
                    .flatMap(l -> l.stream())
                    .map(feedItemAnalyzer::analyze)
                    .collect(Collectors.toList());


            Map<String, List<AnalyzedFeedItem>> frequencyMap = buildFrequencyMap(analyzedItems);
            return mapResult(sortFrequencyMap(frequencyMap));
        } catch (Exception e) {
            throw new RssFeedAnalyzerException("Could not analyze feeds due to " + e.getMessage());
        }

    }

    private Map<String, List<AnalyzedFeedItem>> buildFrequencyMap(List<AnalyzedFeedItem> analyzedItems){
        LOGGER.info("Creating Frequency Map for {} feed items", analyzedItems.size());

        Map<String , List<AnalyzedFeedItem>> result = new HashMap<>();
        for (AnalyzedFeedItem item: analyzedItems){
            for (String keyword : item.getKeyWords()){
                if (result.containsKey(keyword)){
                    result.get(keyword).add(item);
                } else {
                    List<AnalyzedFeedItem> list = new ArrayList<>();
                    list.add(item);
                    result.put(keyword, list);
                }
            }
        }

        return result;
    }

    private List<Map.Entry<String, List<AnalyzedFeedItem>>> sortFrequencyMap(Map<String , List<AnalyzedFeedItem>> frequencyMap){
        Comparator<List> listComparator = Comparator.comparingInt(l -> l.size());
        List<Map.Entry<String, List<AnalyzedFeedItem>>> list = new ArrayList(frequencyMap.entrySet());
        list.sort(Map.Entry.comparingByValue((listComparator).reversed()));
        return list;
    }

    private AnalysisResult mapResult(List<Map.Entry<String, List<AnalyzedFeedItem>>> sortedList) {
        AnalysisResult analysisResult = new AnalysisResult();
        sortedList.stream()
                .limit(relevantHotTopics)
                .map(this::mapHotTopic)
                .forEach(analysisResult::addHotTopic);

        return analysisResult;
    }

    private HotTopic mapHotTopic(Map.Entry<String, List<AnalyzedFeedItem>> itemEntry){
        HotTopic hotTopic = new HotTopic()
                .withKeyword(itemEntry.getKey())
                .withOccurence(itemEntry.getValue().size());

        itemEntry.getValue().stream()
                .map(this::mapFeedItem)
                .forEach(hotTopic::addFeedItem);

        return hotTopic;
    }

    private FeedItem mapFeedItem(AnalyzedFeedItem feedItem) {
        return new FeedItem()
                .withTitle(feedItem.getTitle())
                .withLink(feedItem.getLink());
    }
}
