package nl.utwente.soa.project_service.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.project_service.access.GoalRepository;
import nl.utwente.soa.project_service.access.TaskRepository;
import nl.utwente.soa.project_service.model.Goal;
import nl.utwente.soa.project_service.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service // is same as @Component (a Bean type), but more specific.
public class GoalService {

  private final GoalRepository goalRepository;
  private final TaskRepository taskRepository;

  private final JmsTemplate jmsTemplate;
  @Value("${ActiveMQ.queue.goal}")
  private String goalQueue;

  private final ProjectService projectService;

  @Autowired
  public GoalService(GoalRepository goalRepository, TaskRepository taskRepository,
                     ProjectService projectService, JmsTemplate jmsTemplate) {
    this.goalRepository = goalRepository;
    this.taskRepository = taskRepository;
    this.projectService = projectService;
    this.jmsTemplate = jmsTemplate;
  }

  public List<Goal> getGoals(Long projectId) {
    // throw an exception if the project of the goals does not exist
    try {
      Optional<Project> project = projectService.getProject(projectId);
    } catch (IllegalStateException e) {
      throw e;
    }
    return goalRepository.findAllByProjectId(projectId);
  }

  public Optional<Goal> getGoal(Long projectId, Long goalId) {
    Goal goal = goalRepository.findGoalByProjectIdAndGoalId(projectId, goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with Id " + goalId + " does not exist within this project."
    ));
    // throw an exception if the project of the goal does not exist
    try {
      Optional<Project> project = projectService.getProject(projectId);
    } catch (IllegalStateException e) {
      throw e;
    }
    return goalRepository.findById(goalId);
  }

  public void addNewGoal(Long projectId, Goal goal) {
    // throw an exception if not all required fields are filled in
    if (goal.getName() == null || goal.getDescription() == null || goal.getDeadline() == null) {
      throw new IllegalStateException("The following fields cannot be null: name, description, deadline");
    }
    // throw an exception if an id is specified in the body of the POST request (they should be created by the server)
    if (goal.getProjectId() != null || goal.getGoalId() != null || goal.getId() != null) {
      throw new IllegalStateException("Please remove the following fields from the body of " +
          "the POST request: projectId, goalId, id");
      // The value of the DB id actually doesn't matter, since it is overwritten by the sequence_generator
    }
    // throw an exception if the project of the to-be-created goal does not exist
    try {
      Optional<Project> project = projectService.getProject(projectId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if either the goalId or name of the goal is already used within this project
    List<Goal> goalsWithProjectId = goalRepository.findAllByProjectId(projectId);
    Long max = 0L;
    for (Goal goalWithProjectId : goalsWithProjectId) {
      if (goalWithProjectId.getName().equals(goal.getName())) {
        throw new IllegalStateException("Name of the goal is already used within this project");
      }
      System.out.println("GOAL IN DB: " + goalWithProjectId.getName());
      if (goalWithProjectId.getGoalId() > max) {
        max = goalWithProjectId.getGoalId();
      }
    }
    // throw an exception if the goal deadline is created with a date later than the project deadline
    Date goalDeadline = goal.getDeadline();
    Project project = projectService.getProject(projectId).get();
    Date projectDeadline = project.getDeadline();
    if (goalDeadline.after(projectDeadline)) {
      throw new IllegalStateException("The goal deadline cannot be later than the project deadline!");
    }
    // make sure these fields are set, because they are not specified in the body of the POST request
    goal.setGoalId(max + 1);
    goal.setProjectId(projectId);
    goal.setCompleted(false);
    goalRepository.save(goal);
    jmsTemplate.convertAndSend(goalQueue, goal);
  }

  @Transactional
  public void deleteGoal(Long projectId, Long goalId) {
    Goal goal = goalRepository.findGoalByProjectIdAndGoalId(projectId, goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with Id " + goalId + " does not exist within this project."
    ));
    // throw an exception if the project of the goal does not exist
    try {
      Optional<Project> project = projectService.getProject(projectId);
    } catch (IllegalStateException e) {
      throw e;
    }
    goalRepository.deleteById(goalId);
    // also delete all tasks corresponding to this goal
    taskRepository.deleteAllByProjectIdAndGoalId(projectId, goalId);
  }

  @Transactional
  public void deleteGoals(Long projectId) {
    // throw an exception if the project of the goal does not exist
    try {
      Optional<Project> project = projectService.getProject(projectId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // delete all goals of a project
    goalRepository.deleteAllByProjectId(projectId);
    // also delete all tasks of a project
    taskRepository.deleteAllByProjectId(projectId);

  }

  @Transactional
  public void updateGoal(Long projectId, Long goalId, String name, String description, Date deadline, Boolean completed) {
    Goal goal = goalRepository.findGoalByProjectIdAndGoalId(projectId, goalId).orElseThrow(() -> new IllegalStateException(
        "Goal with id " + goalId + " does not exist within this project"
    ));
    // throw an exception if the project of the goal does not exist
    try {
      Optional<Project> project = projectService.getProject(projectId);
    } catch (IllegalStateException e) {
      throw e;
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
      Project project = projectService.getProject(projectId).get();
      Date projectDeadline = project.getDeadline();
      if (deadline.after(projectDeadline)) {
        throw new IllegalStateException("The goal deadline cannot be later than the project deadline!");
      }
      goal.setDeadline(deadline);
    }

    if (completed != null && !Objects.equals(goal.getCompleted(), completed)) {
      goal.setCompleted(completed);
    }
  }
}
