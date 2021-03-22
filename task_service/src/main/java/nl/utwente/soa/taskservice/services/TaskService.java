package nl.utwente.soa.taskservice.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.taskservice.access.TaskRepository;
import nl.utwente.soa.taskservice.model.Goal;
import nl.utwente.soa.taskservice.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  @Autowired
  private JmsTemplate jmsTemplate;
  @Value("${goalservice.port}")
  private String goalPort;
  @Value("${ActiveMQ.queue.test}")
  private String testQueue;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> getTasks(Long projectId, Long goalId) {
    // throw an exception if the goal of the tasks does not exist
    try {
      // see if the goal with that goalId and projectId can be fetched from the GoalService
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    return taskRepository.findAllByGoalId(goalId);
  }

  public Optional<Task> getTask(Long projectId, Long goalId, Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist."
    ));
    // throw an exception if the goal of the tasks does not exist
    try {
      // see if the goal with that goalId and projectId can be fetched from the GoalService
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if the goalId of the requested task does not match the goalId in the URI
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the goal with id " + goalId);
    }
    return taskRepository.findById(taskId);
  }

  public void addNewTask(Long projectId, Long goalId, Task task) {
    // throw an exception if the id of the task is already used
    if (task.getId() != null && taskRepository.existsById(task.getId())) {
      throw new IllegalStateException("Task ID taken");
    }
    // throw an exception if either the projectId or the goalId of the to-be created task does not exist
    try {
      // see if the goal with that goalId and projectId can be fetched from the GoalService
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if the URI goalId is not equal to the goalId of the body of the HTTP POST request
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("There is a mismatch between the goalId in the URI and the goalId of the newly created task");
    }
    // throw an exception if the name of the task is already used within this goal
    List<Task> tasksWithGoalId = taskRepository.findAllByGoalId(goalId);
    for (Task taskWithGoalId : tasksWithGoalId) {
      if (taskWithGoalId.getName().equals(task.getName())) {
        throw new IllegalStateException("Name of the task is already used in this goal");
      }
    }
    taskRepository.save(task);
    jmsTemplate.convertAndSend(testQueue, task);
  }

  public void deleteTask(Long projectId, Long goalId, Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist."
    ));
    // throw an exception if the goal of the tasks does not exist
    try {
      // see if the goal with that goalId and projectId can be fetched from the GoalService
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if the goalId of the task does not match the goalId in the URI
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the goal with id " + goalId);
    }
    taskRepository.deleteById(taskId);
  }

  public void deleteTasks(Long projectId, Long goalId) {
    // throw an exception if the goal of the tasks does not exist
    try {
      // see if the goal with that goalId and projectId can be fetched from the GoalService
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    taskRepository.deleteAllByGoalId(goalId);
  }

  @Transactional
  public void updateTask(Long projectId, Long goalId, Long taskId, String name, String description, Long goalIdChange, Integer weight, Boolean completed) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException(
        "Task with id " + taskId + " does not exist!"
    ));
    // throw an exception if the goal does not exist
    try {
      // see if the client goal with that new goalIdChange can be fetched from the GoalService
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if the goalId of the task does not match the goalId in the URI
    if (!Objects.equals(task.getGoalId(), goalId)) {
      throw new IllegalStateException("Task with Id " + taskId + " is not part of the goal with id " + goalId);
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
      // throw an exception if the goalIdChange (so the new goal id) does not exist
      try {
        // see if the client goal with that new goalIdChange can be fetched from the GoalService
        Goal newGoal = this.getGoal(projectId, goalIdChange);
      } catch (IllegalStateException e) {
        throw e;
      }
      Goal oldGoal = this.getGoal(projectId, goalId);
      Goal newGoal = this.getGoal(projectId, goalIdChange);
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

  public Goal getGoal(Long projectId, Long goalId) {
    String url = "http://localhost:" + goalPort + "/api/v1/projects/" + projectId + "/goals/" + goalId;
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
