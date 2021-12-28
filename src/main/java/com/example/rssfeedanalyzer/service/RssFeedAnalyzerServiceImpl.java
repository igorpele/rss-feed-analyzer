package com.example.rssfeedanalyzer.service;

import com.example.rssfeedanalyzer.RssFeedAnalyzerException;
import com.example.rssfeedanalyzer.appl.dao.AnalysisResultRepository;
import com.example.rssfeedanalyzer.appl.analyze.FeedCollectionAnalyzer;
import com.example.rssfeedanalyzer.appl.domain.AnalysisResult;
import com.example.rssfeedanalyzer.service.dto.AnalysisResultDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * User: pelesic
 */
@Service
public class RssFeedAnalyzerServiceImpl implements RssFeedAnalyzerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssFeedAnalyzerServiceImpl.class);

    private FeedCollectionAnalyzer feedCollectionAnalyzer;
    private AnalysisResultRepository analysisResultRepository;
    private ModelMapper modelMapper;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public RssFeedAnalyzerServiceImpl(FeedCollectionAnalyzer feedCollectionAnalyzer, AnalysisResultRepository analysisResultRepository,
                                      ModelMapper modelMapper, TransactionTemplate transactionTemplate) {
        this.feedCollectionAnalyzer = feedCollectionAnalyzer;
        this.analysisResultRepository = analysisResultRepository;
        this.modelMapper = modelMapper;
        this.transactionTemplate = transactionTemplate;
    }

    @Async
    @Override
    public CompletableFuture<String> analyzeFeeds(List<String> urls) {
        CompletableFuture<String> result = new CompletableFuture<>();
        result.completeAsync(() -> {
            LOGGER.info("Analyzing RSS Feeds {}", urls);
            String resultId = UUID.randomUUID().toString();
            AnalysisResult analysisResult = feedCollectionAnalyzer.analyzeFeeds(urls);
            analysisResult.setResultId(resultId);
            transactionTemplate.executeWithoutResult((action) -> {
                  analysisResultRepository.save(analysisResult);
                  action.flush();
            });
            return resultId;
        }).exceptionally(throwable -> {
            LOGGER.warn("Exception {}", throwable.getMessage());
            throw ((RssFeedAnalyzerException)throwable);
        });

        return result;
    }

    @Override
    public AnalysisResultDto getAnalysisResult(String resultId) {
        Optional<AnalysisResult> analysisResult = analysisResultRepository.findByResultId(resultId);
        if (analysisResult.isPresent()){
            AnalysisResultDto result = modelMapper.map(analysisResult.get(), AnalysisResultDto.class);
            return result;
        }
        throw new IllegalArgumentException("Result " + resultId + " not found");
    }
}
