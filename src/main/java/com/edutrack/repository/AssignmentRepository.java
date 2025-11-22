package com.edutrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutrack.model.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
