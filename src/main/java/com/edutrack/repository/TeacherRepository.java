package com.edutrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutrack.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
