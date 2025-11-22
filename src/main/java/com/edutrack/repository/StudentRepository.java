package com.edutrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutrack.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
