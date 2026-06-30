package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    /*
     Queries I may want

     findByResumeTitle may be the only one right now
     */


    Optional<Resume> findByIdAndUserId (Long resumeId, Long userId);
    //when I need a specific resume title or create/error if it's not found for a given user
    Optional<Resume> findByResumeTitleAndUserId(String resumeTitle, Long userId);
    List<Resume> findByUserId (Long userId); //shows all Resume's held by the user's ID


    void deleteByIdAndUserId(Long id, Long id1);
}
