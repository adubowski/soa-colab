package com.services.group_control.services;

import com.services.group_control.model.StudentGroup;
import com.services.group_control.model.StudentGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentGroupConfig {

    @Bean
    CommandLineRunner studentGroupCLRunner(StudentGroupRepository repository) {
        return args -> {
            StudentGroup studentGroup1 = new StudentGroup(
                    "group1"
            );

            StudentGroup studentGroup2 = new StudentGroup(
                    "group2"
            );

            repository.saveAll(
                    List.of(studentGroup1, studentGroup2)
            );
        };
    }
}
