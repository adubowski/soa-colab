package nl.utwente.soa.project_service.data;

import java.util.Date;
import nl.utwente.soa.project_service.access.ProjectRepository;
import nl.utwente.soa.project_service.model.Project;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

  @Bean
  CommandLineRunner projectCLRunner(ProjectRepository repository) {
    return args -> {
      Project project = new Project(1L, "MyProject", "This project is amazing!", new Date(), false);
      if (repository.findProjectByName("MyProject").isEmpty()) {
        repository.save(project);
      }
    };
  }
}
