package com.example.rssfeedanalyzer.appl.analyze;

import com.example.rssfeedanalyzer.RssFeedAnalyzerException;
import com.example.rssfeedanalyzer.appl.domain.AnalysisResult;
import com.example.rssfeedanalyzer.appl.domain.FeedItem;
import com.example.rssfeedanalyzer.appl.domain.HotTopic;
import com.example.rssfeedanalyzer.appl.retrieve.RssFeedRetriever;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


            SortedMap<String, List<AnalyzedFeedItem>> frequencyMap = buildFrequencyMap(analyzedItems);
            List<Map.Entry<String, List<AnalyzedFeedItem>>> sortedList = sortFrequencyMap(aggregateMap(frequencyMap));
            return mapResult(sortedList);
        } catch (Exception e) {
            throw new RssFeedAnalyzerException("Could not analyze feeds due to " + e.getMessage());
        }

    }

    private SortedMap<String, List<AnalyzedFeedItem>> buildFrequencyMap(List<AnalyzedFeedItem> analyzedItems){
        LOGGER.info("Creating Frequency Map for {} feed items", analyzedItems.size());

        SortedMap<String , List<AnalyzedFeedItem>> result = new TreeMap<>();
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


    private List<Map.Entry<String, List<AnalyzedFeedItem>>> aggregateMap(
            SortedMap<String, List<AnalyzedFeedItem>> sortedMap){
        List<Map.Entry<String, List<AnalyzedFeedItem>>> result = new ArrayList<>();

        List<Map.Entry<String, List<AnalyzedFeedItem>>> entries = new ArrayList<>(sortedMap.entrySet());
        int index = 0;
        boolean lastAggregated = false;
        while (index < entries.size() - 1) {
            Map.Entry<String, List<AnalyzedFeedItem>> current = entries.get(index);
            int nextIndex = index + 1;
            List<AnalyzedFeedItem> similar;
            while ((similar = mergeSimilar(current.getKey(), entries.get(nextIndex))) != null) {
                current.getValue().addAll(similar);
                if (nextIndex == entries.size() - 1) {
                    lastAggregated = true;
                    break;
                } else {
                    nextIndex++;
                }
            }
            result.add(current);
            index = nextIndex;
        }
        if (!lastAggregated){
            result.add(entries.get(entries.size() - 1));
        }
        return result;
    }

    private List<AnalyzedFeedItem> mergeSimilar(String shorter, Map.Entry<String, List<AnalyzedFeedItem>> next){
        boolean notOnlyDigits = !StringUtils.isNumeric(shorter) && !StringUtils.isNumeric(next.getKey());
        boolean isSimilar = next.getKey().toLowerCase().startsWith(shorter.toLowerCase());
        if (notOnlyDigits && isSimilar) {
            return next.getValue();
        }
        return null;
    }

    private List<Map.Entry<String, List<AnalyzedFeedItem>>> sortFrequencyMap(List<Map.Entry<String , List<AnalyzedFeedItem>>> list){
        Comparator<List> listComparator = Comparator.comparingInt(l -> l.size());
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
