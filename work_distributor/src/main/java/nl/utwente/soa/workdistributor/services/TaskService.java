package nl.utwente.soa.workdistributor.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.workdistributor.access.TaskRepository;
import nl.utwente.soa.workdistributor.model.Goal;
import nl.utwente.soa.workdistributor.model.Project;
import nl.utwente.soa.workdistributor.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> getTasks(Long projectId, Long goalId){
    List<Task> tasks = taskRepository.findAllByGoalId(goalId);
    // throw exception if one of the requested tasks' goalId or projectId doesn't match goalId or projectId in the URI
    for (Task task : tasks) {
      if (!Objects.equals(task.getGoalId(), goalId)) {
        throw new IllegalStateException("Task with Id " + task.getId() + " is not part of the goal with id " + goalId);
      }
      Goal goal = this.getGoal(goalId);
      if (!Objects.equals(goal.getProjectId(), projectId)) {
        throw new IllegalStateException("Task with Id " + task.getId() + " is not part of the project with id " + projectId);
      }
    }
    return tasks;
  }

  public Optional<Task> getTask(Long projectId, Long goalId, Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist."
    ));
    // throw an exception if the goalId of the requested task does not match the goalId in the URI
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the goal with id " + goalId);
    }
    // throw an exception if the projectId of the requested task does not match the projectId in the URI
    Goal goal = this.getGoal(goalId);
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the project with id " + projectId);
    }
    return taskRepository.findById(taskId);
  }

  public void addNewTask(Long projectId, Long goalId, Task task) {
    // throw an exception if the id of the task is already used
    if (task.getId() != null && taskRepository.existsById(task.getId())) {
      throw new IllegalStateException("Task ID taken");
    }
    // throw an exception if the URI goalId is not equal to the goalId of the body of the HTTP POST request
    if (Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("There is a mismatch between the goalId in the URI and the goalId of the newly created task");
    }
    // throw an exception if the URI projectId is not equal to the projectId of the body of the HTTP POST request
    Goal goal = this.getGoal(goalId);
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Task with Id " + task.getId() + " is not part of the project with id " + projectId);
    }
    // throw an exception if the name of the task is already used within this goal
    List<Task> tasksWithGoalId = taskRepository.findAllByGoalId(goalId);
    for (Task taskWithGoalId : tasksWithGoalId) {
      if (taskWithGoalId.getName().equals(task.getName())) {
        throw new IllegalStateException("Name of the task is already used in this goal");
      }
    }
    taskRepository.save(task);
  }

  public void deleteTask(Long projectId, Long goalId, Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist."
    ));
    // throw an exception if the goalId of the task does not match the goalId in the URI
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the goal with id " + goalId);
    }
    // throw an exception if the projectId of the task does not match the projectId in the URI
    Goal goal = this.getGoal(goalId);
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the project with id " + projectId);
    }
    taskRepository.deleteById(taskId);
  }

  @Transactional
  public void updateTask(Long projectId, Long goalId, Long taskId, String name, String description, Long goalIdChange, Integer weight, Boolean completed) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException(
        "Task with id " + taskId + " does not exist!"
    ));
    // throw an exception if the goalId of the task does not match the goalId in the URI
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the goal with id " + goalId);
    }
    // throw an exception if the projectId of the task does not match the projectId in the URI
    Goal goal = this.getGoal(goalId);
    if (!Objects.equals(goal.getProjectId(), projectId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the project with id " + projectId);
    }

    if (name != null && name.length() > 0 && !Objects.equals(task.getName(), name)) {
      // throw an exception if the name of the task is already used within this goal
      List<Task> tasksWithGoalId = taskRepository.findAllByGoalId(goalId);
      for (Task taskWithGoalId : tasksWithGoalId) {
        if (taskWithGoalId.getName().equals(name)) {
          throw new IllegalStateException("This task name is already used in this goal!");
        }
      }
      task.setName(name);
    }

    if (description != null && description.length() > 0 && !Objects.equals(task.getDescription(), description)) {
      task.setDescription(description);
    }

    if (completed != null && !Objects.equals(task.getCompleted(), completed)) {
      task.setCompleted(completed);
    }

    if (weight != null && !Objects.equals(task.getWeight(), weight)) {
      task.setWeight(weight);
    }

    if (goalIdChange != null && !Objects.equals(goalId, goalIdChange)) {
      Goal oldGoal = this.getGoal(goalId);
      Goal newGoal = this.getGoal(goalIdChange);
      if (!Objects.equals(oldGoal.getProjectId(), newGoal.getProjectId())) {
        throw new IllegalStateException("You can only switch a task to another goal of the same project. " +
            "Switching a task to a goal of another project is not allowed.");
      }
      task.setGoalId(goalId);
    }


  }

  // Get information from GoalService (Sync Communication)
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;
  public Goal getGoal(Long goalId) {
    String url = "http://localhost:8080/api/v1/goals/" + goalId;
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      Goal goal = restTemplate.getForObject(url, Goal.class);
      return goal;
    } catch (IllegalStateException e) {
      throw e;
    }
  }

//  @Autowired
//  private RestTemplateBuilder restTemplateBuilder2;
//  public Goal[] getGoals() {
//    String url = "http://localhost:8080/api/v1/goals";
//    RestTemplate restTemplate = restTemplateBuilder2.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
//    try {
//      ResponseEntity<Goal[]> response = restTemplate.getForEntity(url, Goal[].class);
//      Goal[] goals = response.getBody();
//      return goals;
//    } catch (IllegalStateException e) {
//      throw e;
//    }
//  }

}
