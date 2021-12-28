package com.example.rssfeedanalyzer.service

import com.example.rssfeedanalyzer.IntegrationSpecification
import com.example.rssfeedanalyzer.RssFeedAnalyzerException
import com.example.rssfeedanalyzer.appl.dao.AnalysisResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestTemplate

import javax.transaction.Transactional
import java.util.concurrent.CompletionException

/**
 * User: pelesic
 */
@AutoConfigureWebClient
@AutoConfigureMockRestServiceServer
@Transactional
class RssFeedAnalyzerServiceIntTest extends IntegrationSpecification {

    @Autowired
    RssFeedAnalyzerService feedAnalyzerService

    @Autowired
    MockRestServiceServer mockRestServiceServer;

    @Autowired
    RestTemplate restTemplate

    @Autowired
    AnalysisResultRepository repository

    void setup() {
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build()
        mockRestServiceServer.reset()
    }

    void cleanup() {
        mockRestServiceServer.verify()
    }

    def "::test analyze retriever problem"() {
        given:
        mockRestServiceServer.reset()
        mockRestServiceServer
                .expect(MockRestRequestMatchers.requestTo("/feed2"))
                .andRespond(MockRestResponseCreators.withServerError())

        when:
        def analyzed = feedAnalyzerService.analyzeFeeds(["/feed2"])
        analyzed.join()

        then:
        def e = thrown(CompletionException)
        e.cause instanceof RssFeedAnalyzerException

    }

    def "::test analyze feeds"() {
        given:
        mockRestServiceServer
                .expect(MockRestRequestMatchers.requestTo("/feed1"))
                .andRespond(MockRestResponseCreators.withSuccess(goodResponse(), MediaType.APPLICATION_RSS_XML))

        when:
        def analyzed = feedAnalyzerService.analyzeFeeds(["/feed1"])
        def resultID = analyzed.join()
        assert resultID != null

        def result = repository.findByResultId(resultID)

        then:
        result.isPresent() == true
        result.get().hotTopicList.size() == 3

    }

    def "::test get result"() {
        expect:
        feedAnalyzerService.getAnalysisResult("result1")  !=  null
    }

    def "::test no result"() {
        when:
        feedAnalyzerService.getAnalysisResult("noresult")

        then:
        thrown(IllegalArgumentException)
    }

    String goodResponse(){
        return "<rss xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" version=\"2.0\">\n" +
                "<channel xml:base=\"http://www.sportingnews.com/us/rss\">\n" +
                "<language>en-US</language>\n" +
                "<title>Sporting News RSS</title>\n" +
                "<description>Sporting News RSS</description>\n" +
                "<pubDate>Fri, 24 December 2021 07:43:45 -0500</pubDate>\n" +
                "<link>http://www.sportingnews.com/us/rss</link>\n" +
                "<copyright>Copyright 2021 Sporting News</copyright>\n" +
                "<atom:link rel=\"self\" type=\"application/rss+xml\" href=\"http://www.sportingnews.com/us/rss\"/>\n" +
                "<item>\n" +
                "<title>\n" +
                "<![CDATA[ Premier League Boxing Day betting odds, tips & picks for EPL Matchday 19 ]]>\n" +
                "</title>\n" +
                "<description>\n" +
                "<![CDATA[ <p>Check out these tips and picks ahead of one of the most exciting days on the Premier League calendar.</p> ]]>\n" +
                "</description>\n" +
                "<pubDate>Fri, 24 December 2021 07:30:58 -0500</pubDate>\n" +
                "<link>https://www.sportingnews.com/us/soccer/news/premier-league-boxing-day-betting-odds-tips-picks-epl/7ffae5vcidtp1hclxbq9rb9tz</link>\n" +
                "<guid isPermaLink=\"false\">5112346</guid>\n" +
                "<author>Sporting News</author>\n" +
                "<dc:creator>Sporting News</dc:creator>\n" +
                "<slash:comments>0</slash:comments>\n" +
                "<enclosure type=\"image/jpg\" length=\"102400\" url=\"http://images.daznservices.com/di/library/sporting_news/d/c1/aaron-ramsdale-arsenal-antonio-conte-tottenham-christian-pulisic-chelsea_6trjstsi1oh113cmk7cbs0rns.jpeg?t=-342132787\"/>\n" +
                "<media:content height=\"1080\" width=\"1920\" url=\"http://images.daznservices.com/di/library/sporting_news/d/c1/aaron-ramsdale-arsenal-antonio-conte-tottenham-christian-pulisic-chelsea_6trjstsi1oh113cmk7cbs0rns.jpeg?t=-342132787\"/>\n" +
                "<media:credit role=\"owner\">(Getty Images)</media:credit>\n" +
                "<media:description>Boxing Day will feature London clubs Arsenal, Tottenham and Chelsea vying for holiday victories</media:description>\n" +
                "</item></channel></rss>";
    }
}
