package com.example.rssfeedanalyzer.appl.analyze;

import com.rometools.rome.feed.synd.SyndEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * User: pelesic
 */
@Component
public class SimpleFeedItemAnalyzerImpl implements FeedItemAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFeedItemAnalyzerImpl.class);

    private TreeSet<String> stopWordSet ;

    public SimpleFeedItemAnalyzerImpl() throws IOException {
        Path path = ResourceUtils.getFile("classpath:en_stopwords.txt").toPath();
        this.stopWordSet = Files.readAllLines(path).stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>()));


    }

    @Override
    @Cacheable(cacheNames = "feeditems", key = "#entry.title")
    public AnalyzedFeedItem analyze(SyndEntry entry) {
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("title {}", entry.getTitle());
        }
        String replace;
        if (entry.getSource() != null ){
            replace = entry.getTitle().replace(entry.getSource().getTitle(), "");
        } else {
            replace = entry.getTitle();
        }
        List<String> replacedList = List.of(replace.split(" "));
        List<String> result = replacedList.stream()
                .map(str -> str.replaceAll("[^\\w\\s]", "").trim())
                .filter(str -> !stopWordSet.contains(str.toLowerCase()))
                .filter(str -> str.length() > 2)
                .collect(Collectors.toList());

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("keywords {}", result);
        }

        return new AnalyzedFeedItem(entry.getTitle(), entry.getLink(), result);
    }

}
