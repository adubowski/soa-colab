package nl.utwente.soa.project_service.web;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.project_service.model.Task;
import nl.utwente.soa.project_service.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/projects/{projectId}/goals/{goalId}/tasks")
public class TaskController {

  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> getTasks(@PathVariable("projectId") Long projectId,
                             @PathVariable("goalId") Long goalId) {
    return taskService.getTasks(projectId, goalId);
  }

  @GetMapping("{taskId}")
  public Optional<Task> getTask(@PathVariable("projectId") Long projectId,
                                @PathVariable("goalId") Long goalId,
                                @PathVariable("taskId") Long taskId) {
    return taskService.getTask(projectId, goalId, taskId);
  }

  @PostMapping
  public void createNewTask(@PathVariable("projectId") Long projectId,
                            @PathVariable("goalId") Long goalId,
                            @RequestBody Task task) {
    taskService.addNewTask(projectId, goalId, task);
  }

  @DeleteMapping("{taskId}")
  public void deleteTask(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId,
                         @PathVariable("taskId") Long taskId) {
    taskService.deleteTask(projectId, goalId, taskId);
  }

  @DeleteMapping
  public void deleteTasks(@PathVariable("projectId") Long projectId,
                          @PathVariable("goalId") Long goalId) {
    taskService.deleteTasks(projectId, goalId);
  }

  @PutMapping("{taskId}")
  public void updateTask(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long currentGoalId,
                         @PathVariable("taskId") Long taskId,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) Long goalId,
                         @RequestParam(required = false) Integer weight,
                         @RequestParam(required = false) Boolean completed) {
    taskService.updateTask(projectId, currentGoalId, taskId, name, description, goalId, weight, completed);
  }
}
