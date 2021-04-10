package nl.utwente.soa.group_service.services;

import nl.utwente.soa.group_service.model.StudentGroup;
import nl.utwente.soa.group_service.model.StudentGroupRepository;
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
