package nl.utwente.soa.workdistributor.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.workdistributor.model.Goal;
import nl.utwente.soa.workdistributor.access.GoalRepository;
import nl.utwente.soa.workdistributor.model.Project;
import nl.utwente.soa.workdistributor.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service // is same as @Component (a Bean type), but more specific.
public class GoalService {

  private final GoalRepository goalRepository;

  @Autowired
  public GoalService(GoalRepository goalRepository) {
    this.goalRepository = goalRepository;
  }

  public List<Goal> getGoals(Long projectId){
    List<Goal> goals = goalRepository.findAllByProjectId(projectId);
    // throw exception if one of the requested goals' projectId doesn't match projectId in the URI
    for (Goal goal : goals) {
      if (!Objects.equals(goal.getProjectId(), projectId)) {
        throw new IllegalStateException("Goal with Id " + goal.getId() + " is not part of the project with id " + projectId);
      }
    }
    return goalRepository.findAllByProjectId(projectId);
  }

  public Optional<Goal> getGoal(Long projectId, Long goalId) {
    Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with Id " + goalId + " does not exist."
    ));
    // throw an exception if the projectId of the goal does not match the projectId in the URI
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Goal with Id " + goalId + " is not part of the project with id " + projectId);
    }
    return goalRepository.findById(goalId);
  }

  public void addNewGoal(Long projectId, Goal goal) {
    // throw an exception if the id of the goal is already used
    if (goal.getId() != null && goalRepository.existsById(goal.getId())) {
      throw new IllegalStateException("Goal ID taken");
    }
    // throw an exception of the URI projectId is not equal to the projectId of the body of the HTTP POST request
    if (Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("There is a mismatch between the projectId in the URI and the projectId of the newly created goal");
    }
    // throw an exception if the name of the goal is already used within this project
    List<Goal> goalsWithProjectId = goalRepository.findAllByProjectId(projectId);
    for (Goal goalWithProjectId : goalsWithProjectId) {
      if (goalWithProjectId.getName().equals(goal.getName())) {
        throw new IllegalStateException("Name of the goal is already used in this project");
      }
    }
    // throw an exception if the goal deadline is created with a date later than the project deadline
    Date goalDeadline = goal.getDeadline();
    Date projectDeadline = this.getProject(projectId).getDeadline();
    if (goalDeadline.after(projectDeadline)) {
      throw new IllegalStateException("The goal deadline cannot be later than the project deadline!");
    }
    goalRepository.save(goal);
  }

  public void deleteGoal(Long projectId, Long goalId) {
    Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with Id " + goalId + " does not exist."
    ));
    // throw an exception if the projectId of the goal does not match the project Id in the URI
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Goal with Id " + goalId + " is not part of the project with id " + projectId);
    }
    goalRepository.deleteById(goalId);
  }

  @Transactional
  public void updateGoal(Long projectId, Long goalId, String name, String description, Date deadline, Boolean completed) {
    Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with id " + goalId + " does not exist!"
    ));
    // throw an exception if the projectId of the goal does not match the project Id in the URI
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Goal with Id " + goalId + " is not part of the project with id " + projectId);
    }

    if (name != null && name.length() > 0 && !Objects.equals(goal.getName(), name)) {
      // throw an exception if the name of the goal is already used within this project
      List<Goal> goalsWithProjectId = goalRepository.findAllByProjectId(projectId);
      for (Goal goalWithProjectId : goalsWithProjectId) {
        if (goalWithProjectId.getName().equals(name)) {
          throw new IllegalStateException("This goal name is already used in this project!");
        }
      }
      goal.setName(name);
    }

    if (description != null && description.length() > 0 && !Objects.equals(goal.getDescription(), description)) {
      goal.setDescription(description);
    }

    if (deadline != null && !Objects.equals(goal.getDeadline(), deadline)) {
      // throw an exception if the goal deadline is updated to a date later than the project deadline
      Date projectDeadline = this.getProject(projectId).getDeadline();
      if (deadline.after(projectDeadline)) {
        throw new IllegalStateException("The goal deadline cannot be later than the project deadline!");
      }
      goal.setDeadline(deadline);
    }

    if (completed != null && !Objects.equals(goal.getCompleted(), completed)) {
      goal.setCompleted(completed);
    }
  }

  // Get information from ProjectService (Sync Communication)
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;
  public Project getProject(Long projectId) {
    String url = "http://localhost:8080/api/v1/projects/" + projectId;
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      Project project = restTemplate.getForObject(url, Project.class);
      return project;
    } catch (IllegalStateException e) {
      throw e;
    }
  }
}
