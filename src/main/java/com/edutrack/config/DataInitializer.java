package com.edutrack.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.edutrack.model.User;
import com.edutrack.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Create Teacher if not exists
        if (userRepository.findByEmail("teacher@test.com") == null) {
            User teacher = new User("teacher@test.com", "1234", "TEACHER");
            userRepository.save(teacher);
            System.out.println("✅ Default Teacher created: teacher@test.com / 1234");
        }

        // Create Student if not exists
        if (userRepository.findByEmail("student@test.com") == null) {
            User student = new User("student@test.com", "1234", "STUDENT");
            userRepository.save(student);
            System.out.println("✅ Default Student created: student@test.com / 1234");
        }
    }
}
