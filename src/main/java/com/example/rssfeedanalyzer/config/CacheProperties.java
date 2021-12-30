package com.example.rssfeedanalyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * User: pelesic
 */
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private CacheProperty feeditems;
    private CacheProperty restfeed;

    public CacheProperty getFeeditems() {
        return feeditems;
    }

    public void setFeeditems(CacheProperty feeditems) {
        this.feeditems = feeditems;
    }

    public CacheProperty getRestfeed() {
        return restfeed;
    }

    public void setRestfeed(CacheProperty restfeed) {
        this.restfeed = restfeed;
    }

    static class CacheProperty {

        private int secondsToLive;
        private int maxEntries;

        public int getSecondsToLive() {
            return secondsToLive;
        }

        public void setSecondsToLive(int secondsToLive) {
            this.secondsToLive = secondsToLive;
        }

        public int getMaxEntries() {
            return maxEntries;
        }

        public void setMaxEntries(int maxEntries) {
            this.maxEntries = maxEntries;
        }
    }
}
