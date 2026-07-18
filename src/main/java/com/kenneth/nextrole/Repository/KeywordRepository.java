package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {


    boolean existsByKeyword(String keyword);
    Optional<Keyword> findByKeyword(String keyword);}
