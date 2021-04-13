package nl.utwente.soa.project_service.data;

import java.util.List;
import nl.utwente.soa.project_service.access.TaskRepository;
import nl.utwente.soa.project_service.model.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {
  @Bean
  CommandLineRunner taskCLRunner(TaskRepository repository) {
    return args -> {
      Task task1 = new Task(1L, 1L, 1L,"task1", "This is a cool task", 5, true);
      Task task2 = new Task(2L, 1L, 1L, "task2", "This is a stupid task", 3, false);
      Task task3 = new Task(1L, 2L, 1L, "task1", "This task is easy", 2, false);
      if (repository.findAllByProjectIdAndGoalId(1L, 1L).isEmpty()) {
        repository.saveAll(List.of(task1, task2, task3));
      }
    };
  }
}
