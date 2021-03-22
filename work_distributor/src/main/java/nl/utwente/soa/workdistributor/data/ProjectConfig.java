package nl.utwente.soa.workdistributor.data;

import java.util.Date;
import nl.utwente.soa.workdistributor.model.Project;
import nl.utwente.soa.workdistributor.access.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

  @Bean
  CommandLineRunner projectCLRunner(ProjectRepository repository) {
    return args -> {
      Project project = new Project(1L, 1L, "MyProject", "This project is amazing!", new Date(), false);
      repository.save(project);
    };
  }
}
