package com.example.rssfeedanalyzer.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType
import spock.lang.Specification

/**
 * User: pelesic
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RssFeedAnalyzerControllerTest extends Specification {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    def "::test new analysis not enough urls"() {
        when:
        def headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<List<String>> request = new HttpEntity<List<String>>(["url1"], headers);

        def response = restTemplate.postForEntity("http://localhost:" + port + "/analyze/new", request,  String.class)

        then:
        response != null
        response.statusCodeValue == 400
        
    }

    def "::test invalid urls"() {
        when:
        def headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> request = new HttpEntity<List<String>>(["http://url2", "http://url2"], headers);

        def response = restTemplate.postForEntity("http://localhost:" + port + "/analyze/new", request,  String.class)

        then:
        response != null
        response.statusCodeValue == 400
        response.body.contains("Could not analyze feeds due to Retrieving RSS feed")

    }

    def "::test get invalid result"() {
        when:


        def response = restTemplate.getForEntity("http://localhost:" + port + "/frequency/new",  String.class)

        then:
        response != null
        response.statusCodeValue == 400
        response.body.contains("Result new not found")

    }

    def "::test get result"() {
        when:


        def response = restTemplate.getForEntity("http://localhost:" + port + "/frequency/result1",  String.class)

        then:
        response != null
        response.statusCodeValue == 200

    }
}
