package nl.utwente.soa.project_service.data;

import java.util.Calendar;
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
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 7);
    dt = c.getTime();
    final Date nextWeek = dt;
    return args -> {
      Goal goal1 = new Goal(1L, 1L, "goal1", "this is our first goal!", nextWeek, true);
      Goal goal2 = new Goal(2L, 1L, "goal2", "this is our second goal!", nextWeek, false);
      Goal goal3 = new Goal(3L, 1L,"goal3", "this is our third goal!", nextWeek, false);
      if (repository.findAllByProjectId(1L).size() == 0) {
        repository.saveAll(List.of(goal1, goal2, goal3));
      }
    };
  }

}
