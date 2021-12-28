package com.example.rssfeedanalyzer.appl.retrieve;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * User: pelesic
 */
@Component
public class RssFeedRetrieverImpl implements RssFeedRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssFeedRetrieverImpl.class);

    private RestTemplate restTemplate;

    @Autowired
    public RssFeedRetrieverImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SyndFeed retrieveFeed(String url) throws RssFeedRetrievingException {
        LOGGER.info("Retrieve RSS feed from {}", url);
        SyndFeed result = null;
        try {
            result = restTemplate.execute(url, HttpMethod.GET, null, response -> {
                LOGGER.info("Received result {} from {}", response.getRawStatusCode(), url);
                SyndFeedInput input = new SyndFeedInput();
                try {
                    return input.build(new XmlReader(response.getBody()));
                } catch (Exception e) {
                    LOGGER.warn("RSS Parsing failed for {} due to {}", url, e.getMessage());
                    throw new RssFeedRetrievingException("Parsing RSS feed failed", e);
                }
            });
        } catch (RestClientException e) {
            LOGGER.warn("RestTemplate connection failed for {} due to {}", url, e.getMessage());
            throw new RssFeedRetrievingException("Retrieving RSS feed failed", e);
        }
        return result;
    }
}
