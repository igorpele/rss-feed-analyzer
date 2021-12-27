package com.example.rssfeedanalyzer.appl.analyze

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import spock.lang.Specification

/**
 * User: pelesic
 */
class SimpleFeedItemAnalyzerImplTest extends Specification {

    SimpleFeedItemAnalyzerImpl analyzer = new SimpleFeedItemAnalyzerImpl()

    def "::test simple analyzing"() {
        given:
        SyndEntry entry = Mock()
        entry.getTitle() >> "Hamilton or Verstappen: Who is ESPN's F1 driver of the year"

        when:
        def analyzed = analyzer.analyze(entry)

        then:
        analyzed.keyWords == ["Hamilton", "Verstappen", "ESPNs", "F1", "driver", "year"]
    }

    def "::test analyzing remove source within text"() {
        given:
        SyndEntry entry = Mock()
        entry.getTitle() >> "'Quite hopeful': Abortion pill decision could reshape reproductive health war - AP"

        SyndFeed source = Mock()
        source.getTitle() >> "AP"

        entry.getSource() >> source

        when:
        def analyzed = analyzer.analyze(entry)

        then:
        analyzed.keyWords == ["Quite", "hopeful", "Abortion", "decision", "reshape", "reproductive", "health"]
    }


}
