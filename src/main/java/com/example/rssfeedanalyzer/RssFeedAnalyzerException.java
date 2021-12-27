package com.example.rssfeedanalyzer;

import org.springframework.http.HttpStatus;

/**
 * User: pelesic
 */
public class RssFeedAnalyzerException extends RuntimeException{

    private HttpStatus status;

    public RssFeedAnalyzerException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
