package com.edutrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutrack.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
