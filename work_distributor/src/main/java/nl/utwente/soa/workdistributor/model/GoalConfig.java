package nl.utwente.soa.workdistributor.model;

import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoalConfig {

  @Bean
  CommandLineRunner commandLineRunner(GoalRepository repository) {
    return args -> {
      Goal goal1 = new Goal(1L, "goal1", "this is our first goal!", new Date(), "myProject", true);
      Goal goal2 = new Goal(2L, "goal2", "this is our second goal!", new Date(), "yourProject", false);
      repository.saveAll(List.of(goal1, goal2));
    };
  }

}
