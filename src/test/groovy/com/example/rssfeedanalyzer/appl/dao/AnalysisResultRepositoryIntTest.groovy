package com.example.rssfeedanalyzer.appl.dao

import com.example.rssfeedanalyzer.IntegrationSpecification
import com.example.rssfeedanalyzer.appl.domain.AnalysisResult
import com.example.rssfeedanalyzer.appl.domain.FeedItem
import com.example.rssfeedanalyzer.appl.domain.HotTopic
import org.springframework.beans.factory.annotation.Autowired

import javax.transaction.Transactional

/**
 * User: pelesic
 * Date: 27.12.21
 * Time: 15:18
 */
@Transactional
class AnalysisResultRepositoryIntTest extends IntegrationSpecification {

    @Autowired
    AnalysisResultRepository repository

    def "::get result"() {
        when:
        def result = repository.findByResultId("result1")

        then:
        result.isPresent() == true
        result.get().hotTopicList.size() == 2
        result.get().hotTopicList*.keyword == ["keyword2", "keyword1"]
    }

    def "::save analysis result"() {
        given:
        def feedItem1 = new FeedItem().withLink("link12").withTitle("title12")
        def feedItem2 = new FeedItem().withLink("link22").withTitle("title22")

        def hotTopic = new HotTopic().withKeyword("keyword1222").withItemsList([feedItem1, feedItem2])

        def result = new AnalysisResult().withResultId("result2").withHotTopicList([hotTopic])

        when:
        repository.save(result)
        def savedResult = repository.findByResultId("result2")

        then:
        savedResult.isPresent() == true
        savedResult.get().resultId == "result2"
        savedResult.get().hotTopicList.size() == 1


    }
}
