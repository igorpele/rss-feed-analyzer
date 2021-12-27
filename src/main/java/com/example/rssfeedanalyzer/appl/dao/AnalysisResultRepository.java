package com.example.rssfeedanalyzer.appl.dao;

import com.example.rssfeedanalyzer.appl.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User: pelesic
 */
@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    Optional<AnalysisResult> findByResultId(String resultId);

}
