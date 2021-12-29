package com.example.rssfeedanalyzer.rest;

import com.example.rssfeedanalyzer.RssFeedAnalyzerException;
import com.example.rssfeedanalyzer.service.RssFeedAnalyzerService;
import com.example.rssfeedanalyzer.service.dto.AnalysisResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * User: pelesic
 */
@RestController
public class RssFeedAnalyzerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssFeedAnalyzerController.class);

    private RssFeedAnalyzerService rssFeedAnalyzerService;

    @Autowired
    public RssFeedAnalyzerController(RssFeedAnalyzerService rssFeedAnalyzerService) {
        this.rssFeedAnalyzerService = rssFeedAnalyzerService;
    }

    @PostMapping(value = "/analyze/new", consumes = "application/json")
    ResponseEntity<String> newFeed(@RequestBody List<String> urls) {
        Assert.isTrue(urls != null && urls.size() > 1, "At least two urls required.");
        LOGGER.info("Analyze urls {}", urls);
        CompletableFuture<String> future = rssFeedAnalyzerService.analyzeFeeds(urls);
        String resultId = future.join();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/frequency/" + resultId)
                .body(resultId);
    }

    @GetMapping(value = "/frequency/{id}", produces = "application/json")
    AnalysisResultDto getResult(@PathVariable String id){
        LOGGER.info("Get Analysis Result for {}", id);
        return rssFeedAnalyzerService.getAnalysisResult(id);
    }

    @ExceptionHandler({IllegalArgumentException.class, CompletionException.class, RssFeedAnalyzerException.class})
    public ResponseEntity<String> illegalArgument(Exception ex) {
        LOGGER.debug("Exception {}", ex.getMessage());
        LOGGER.trace("Exception {}", ex);
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

}
