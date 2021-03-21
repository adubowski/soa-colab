package nl.utwente.soa.workdistributor.data;

import java.util.List;
import nl.utwente.soa.workdistributor.access.TaskRepository;
import nl.utwente.soa.workdistributor.model.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {
  @Bean
  CommandLineRunner taskCLRunner(TaskRepository repository) {
    return args -> {
      Task task1 = new Task(1L, "task1", "This is a cool task", 1L, 5, true);
      Task task2 = new Task(2L, "task2", "This is a stupid task", 1L, 3, false);
      Task task3 = new Task(2L, "task1", "This task is easy", 2L, 2, false);
      repository.saveAll(List.of(task1, task2, task3));
    };
  }
}
