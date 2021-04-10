package nl.utwente.soa.project_service.data;

import java.util.Date;
import java.util.List;
import nl.utwente.soa.project_service.access.GoalRepository;
import nl.utwente.soa.project_service.model.Goal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoalConfig {

  @Bean
  CommandLineRunner goalCLRunner(GoalRepository repository) {
    return args -> {
      Goal goal1 = new Goal(1L, 1L, "goal1", "this is our first goal!", new Date(), true);
      Goal goal2 = new Goal(2L, 1L, "goal2", "this is our second goal!", new Date(), false);
      Goal goal3 = new Goal(3L, 1L,"goal3", "this is our third goal!", new Date(), false);
      repository.saveAll(List.of(goal1, goal2, goal3));
    };
  }

}
