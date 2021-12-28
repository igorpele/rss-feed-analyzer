package com.example.rssfeedanalyzer.appl.analyze;

import com.rometools.rome.feed.synd.SyndEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: pelesic
 */
@Component
public class SimpleFeedItemAnalyzerImpl implements FeedItemAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFeedItemAnalyzerImpl.class);

    private String stopWords;

    public SimpleFeedItemAnalyzerImpl() throws IOException {
        this.stopWords = new String(Files.readAllBytes(ResourceUtils.getFile("classpath:en_stopwords.txt").toPath()));
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
                .filter(str -> !stopWords.contains(str.toLowerCase()))
                .collect(Collectors.toList());

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("keywords {}", result);
        }

        return new AnalyzedFeedItem(entry.getTitle(), entry.getLink(), result);
    }

}
