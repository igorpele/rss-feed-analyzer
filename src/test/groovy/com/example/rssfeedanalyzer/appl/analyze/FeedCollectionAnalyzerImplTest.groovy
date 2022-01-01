package com.example.rssfeedanalyzer.appl.analyze

import com.example.rssfeedanalyzer.RssFeedAnalyzerException
import com.example.rssfeedanalyzer.appl.retrieve.RssFeedRetriever
import com.example.rssfeedanalyzer.appl.retrieve.RssFeedRetrievingException
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import spock.lang.Specification

/**
 * User: pelesic
 */
class FeedCollectionAnalyzerImplTest extends Specification {

    FeedCollectionAnalyzer analyzer

    RssFeedRetriever retriever = Mock()
    FeedItemAnalyzer feedItemAnalyzer = Mock()

    void setup() {
        analyzer = new FeedCollectionAnalyzerImpl(retriever, feedItemAnalyzer)
        analyzer.relevantHotTopics = 3
    }

    def "::test analyze feeds"() {
        given:
        def ai1 = new AnalyzedFeedItem("title1", "link1", ["kw1", "kw2"])
        def ai2 = new AnalyzedFeedItem("title2", "link2", ["kw3", "kw2"])
        def ai3 = new AnalyzedFeedItem("title3", "link3", ["kw3", "kw2"])
        def ai4 = new AnalyzedFeedItem("title4", "link4", ["kw4", "kw3"])
        def ai5 = new AnalyzedFeedItem("title5", "link6", ["kw4", "kw2"])

        SyndFeed sf1 = Mock()
        SyndEntry se11 = Mock()
        SyndEntry se12 = Mock()
        sf1.getEntries() >> [se11, se12]

        SyndFeed sf2 = Mock()
        SyndEntry se21 = Mock()
        SyndEntry se22 = Mock()
        SyndEntry se23 = Mock()
        sf2.getEntries() >> [se21, se22, se23]

        when:
        def result = analyzer.analyzeFeeds(["url1", "url2"])

        then:

        result.hotTopicList.size() == 3
        result.hotTopicList*.keyword == ["kw2", "kw3", "kw4"]

        2 * retriever.retrieveFeed(_) >> sf1 >> sf2
        5 * feedItemAnalyzer.analyze(_) >> ai1 >> ai2 >> ai3 >> ai4 >> ai5

    }

    def "::test analyze feed aggregation"() {
        given:
        def ai1 = new AnalyzedFeedItem("title1", "link1", ["kw1", "kw2"])
        def ai2 = new AnalyzedFeedItem("title2", "link2", ["kw33", "kw2"])
        def ai3 = new AnalyzedFeedItem("title3", "link3", ["kw3", "kw2"])
        def ai4 = new AnalyzedFeedItem("title4", "link4", ["kw4", "kw3332"])
        def ai5 = new AnalyzedFeedItem("title5", "link6", ["kw44", "kw2"])

        SyndFeed sf1 = Mock()
        SyndEntry se11 = Mock()
        SyndEntry se12 = Mock()
        sf1.getEntries() >> [se11, se12]

        SyndFeed sf2 = Mock()
        SyndEntry se21 = Mock()
        SyndEntry se22 = Mock()
        SyndEntry se23 = Mock()
        sf2.getEntries() >> [se21, se22, se23]

        when:
        def result = analyzer.analyzeFeeds(["url1", "url2"])

        then:

        result.hotTopicList.size() == 3
        result.hotTopicList*.keyword == ["kw2", "kw3", "kw4"]

        2 * retriever.retrieveFeed(_) >> sf1 >> sf2
        5 * feedItemAnalyzer.analyze(_) >> ai1 >> ai2 >> ai3 >> ai4 >> ai5

    }


    def "::test analyze feeds empty keyword list"() {
        given:
        def ai1 = new AnalyzedFeedItem("title1", "link1", ["kw1", "kw2"])
        def ai2 = new AnalyzedFeedItem("title2", "link2", [])
        def ai3 = new AnalyzedFeedItem("title3", "link3", ["kw2", "kw2"])

        SyndFeed sf1 = Mock()
        SyndEntry se11 = Mock()
        SyndEntry se12 = Mock()
        sf1.getEntries() >> [se11, se12]

        SyndFeed sf2 = Mock()
        SyndEntry se21 = Mock()
        sf2.getEntries() >> [se21]

        when:
        def result = analyzer.analyzeFeeds(["url1", "url2"])

        then:

        result.hotTopicList.size() == 2
        result.hotTopicList*.keyword == ["kw2", "kw1"]

        2 * retriever.retrieveFeed(_) >> sf1 >> sf2
        3 * feedItemAnalyzer.analyze(_) >> ai1 >> ai2 >> ai3

    }

    def "::test analyze retriever exception"() {
        given:
        def ai1 = new AnalyzedFeedItem("title1", "link1", ["kw1", "kw2"])
        def ai2 = new AnalyzedFeedItem("title2", "link2", [])
        def ai3 = new AnalyzedFeedItem("title3", "link3", ["kw2", "kw2"])

        SyndFeed sf1 = Mock()
        SyndEntry se11 = Mock()
        SyndEntry se12 = Mock()
        sf1.getEntries() >> [se11, se12]

        SyndFeed sf2 = Mock()
        SyndEntry se21 = Mock()
        sf2.getEntries() >> [se21]

        when:
        def result = analyzer.analyzeFeeds(["url1", "url2"])

        then:
        thrown(RssFeedAnalyzerException)

        2 * retriever.retrieveFeed(_) >> sf1 >> { throw new RssFeedRetrievingException("failure", null)}
        2 * feedItemAnalyzer.analyze(_) >> ai1 >> ai2 >> ai3

    }
}
