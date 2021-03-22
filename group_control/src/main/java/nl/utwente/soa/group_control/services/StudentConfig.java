package nl.utwente.soa.group_control.services;

import nl.utwente.soa.group_control.model.Student;
import nl.utwente.soa.group_control.model.StudentRepository;
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
          "adam@gmail.com",
          1L
      );

      Student matthijs = new Student(
          "Matthijs",
          "matthijs@gmail.com"
      );

      repository.saveAll(
          List.of(adam, matthijs)
      );
    };
  }
}
