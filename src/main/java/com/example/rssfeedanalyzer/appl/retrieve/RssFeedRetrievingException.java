package com.example.rssfeedanalyzer.appl.retrieve;

/**
 * User: pelesic
 * Date: 27.12.21
 * Time: 13:59
 */
public class RssFeedRetrievingException  extends  RuntimeException{

    public RssFeedRetrievingException(String message, Throwable e) {
        super(message, e);
    }
}
