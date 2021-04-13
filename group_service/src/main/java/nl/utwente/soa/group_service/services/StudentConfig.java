package nl.utwente.soa.group_service.services;

import nl.utwente.soa.group_service.model.Student;
import nl.utwente.soa.group_service.model.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class StudentConfig {

  @Bean
  CommandLineRunner studentCLRunner(StudentRepository repository) {
    return args -> {
      Student adam = new Student(
          "Adam",
          "adam@gmail.com"
      );

      Student matthijs = new Student(
          "Matthijs",
          "matthijs@gmail.com"
      );
      if (repository.findStudentByEmail("adam@gmail.com").isEmpty()
              && repository.findStudentByEmail("matthijs@gmail.com").isEmpty()) {
        repository.saveAll(List.of(adam, matthijs));
      }
    };
  }
}
