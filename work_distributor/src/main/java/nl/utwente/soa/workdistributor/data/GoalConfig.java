package nl.utwente.soa.workdistributor.data;

import java.util.Date;
import java.util.List;
import nl.utwente.soa.workdistributor.model.Goal;
import nl.utwente.soa.workdistributor.access.GoalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoalConfig {

  @Bean
  CommandLineRunner goalCLRunner(GoalRepository repository) {
    return args -> {
      Goal goal1 = new Goal(1L, "goal1", "this is our first goal!", new Date(), 1L, true);
      Goal goal2 = new Goal(2L, "goal2", "this is our second goal!", new Date(), 1L, false);
      Goal goal3 = new Goal(3L, "goal3", "this is our third goal!", new Date(), 2L, false);
      repository.saveAll(List.of(goal1, goal2, goal3));
    };
  }

}
