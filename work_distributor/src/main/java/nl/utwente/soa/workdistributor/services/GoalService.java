package nl.utwente.soa.workdistributor.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.workdistributor.model.Goal;
import nl.utwente.soa.workdistributor.model.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // is same as @Component (a Bean type), but more specific.
public class GoalService {

  private final GoalRepository goalRepository;

  @Autowired
  public GoalService(GoalRepository goalRepository) {
    this.goalRepository = goalRepository;
  }

  public List<Goal> getGoals(){
    return goalRepository.findAll();
  }

  public void addNewGoal(Goal goal) {
    // throw an exception if the name of tha goal is already used
    Optional<Goal> goalOptional = goalRepository.findGoalByName(goal.getName());
    if (goalOptional.isPresent()) {
      throw new IllegalStateException("Name is already used");
    }
    goalRepository.save(goal);
  }

  public void deleteStudent(Long goalId) {
    boolean exists = goalRepository.existsById(goalId);
    if (!exists) {
      throw new IllegalStateException("Goal with id " + goalId + " does not exist!");
    }
    goalRepository.deleteById(goalId);

  }

  @Transactional
  public void updateGoal(Long goalId, String name, String description, Date deadline, boolean completed) {
    Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with id " + goalId + " does not exist!"
    ));

    if (name != null && name.length() > 0 && !Objects.equals(goal.getName(), name)) {
      Optional<Goal> goalOptional = goalRepository.findGoalByName(name);
      if (goalOptional.isPresent()) {
        throw new IllegalStateException("This goal name is already used!");
      }
      goal.setName(name);
    }


    if (description != null && description.length() > 0 && !Objects.equals(goal.getDescription(), description)) {
      goal.setDescription(description);
    }
  }
}
