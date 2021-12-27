package com.example.rssfeedanalyzer

import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * User: pelesic
 */
@ContextConfiguration(loader = SpringBootContextLoader.class)
@SpringBootTest
@ActiveProfiles("integration")
abstract class IntegrationSpecification extends Specification{
}
