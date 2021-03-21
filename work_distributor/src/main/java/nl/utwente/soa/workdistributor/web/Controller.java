package nl.utwente.soa.workdistributor.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.utwente.soa.workdistributor.model.Goal;
import nl.utwente.soa.workdistributor.model.Project;
import nl.utwente.soa.workdistributor.model.Task;
import nl.utwente.soa.workdistributor.services.GoalService;
import nl.utwente.soa.workdistributor.services.ProjectService;
import nl.utwente.soa.workdistributor.services.TaskService;
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
@RequestMapping(path = "api/v1/projects")
public class Controller {

  private final ProjectService projectService;
  private final GoalService goalService;
  private final TaskService taskService;

  @Autowired
  public Controller(ProjectService projectService, GoalService goalService, TaskService taskService) {
    this.projectService = projectService;
    this.goalService = goalService;
    this.taskService = taskService;
  }

  /**
   * PROJECTS --------------------------------------------------------------------------------------
   */

  @GetMapping
  public List<Project> getProjects() {
    return projectService.getProjects();
  }

  @GetMapping(path = "{projectId}")
  public Optional<Project> getProject(@PathVariable("projectId") Long projectId) {
    return projectService.getProject(projectId);
  }

  @PostMapping
  public void createNewProject(@RequestBody Project project) {
    projectService.createNewProject(project);
  }

  @DeleteMapping(path = "{projectId}")
  public void deleteProject(@PathVariable("projectId") Long projectId) {
    projectService.deleteProject(projectId);
  }

  /* It is not possible for a client to update studentGroupId, since a client should not be able
  to create projects for other student groups */
  @PutMapping(path = "{projectId}")
  public void updateProject(@PathVariable("projectId") Long projectId,
                            @RequestParam(required=false) String name,
                            @RequestParam(required=false) String description,
                            @RequestParam(required=false) Date deadline,
                            @RequestParam(required=false) Boolean completed) {
    projectService.updateProject(projectId, name, description, deadline, completed);
  }

  /**
   * GOALS -----------------------------------------------------------------------------------------
   */
  private final String goalsURI = "{projectId}/goals";

  @GetMapping(path = goalsURI)
  public List<Goal> getGoals(@PathVariable("projectId") Long projectId) {
    return goalService.getGoals(projectId);
  }

  @GetMapping(path = goalsURI + "/{goalId}")
  public Optional<Goal> getGoal(@PathVariable("projectId") Long projectId,
                                @PathVariable("goalId") Long goalId) {
    return goalService.getGoal(projectId, goalId);
  }

  @PostMapping(path = goalsURI)
  public void createNewGoal(@PathVariable("projectId") Long projectId,
                            @RequestBody Goal goal) {
    goalService.addNewGoal(projectId, goal);
  }

  @DeleteMapping(path = goalsURI + "/{goalId}")
  public void deleteGoal(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId) {
    goalService.deleteGoal(projectId, goalId);
  }

  @PutMapping(path = goalsURI + "/{goalId}")
  public void updateGoal(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId,
                         @RequestParam(required=false) String name,
                         @RequestParam(required=false) String description,
                         @RequestParam(required=false) Date deadline,
                         @RequestParam(required=false) Boolean completed) {
    goalService.updateGoal(projectId, goalId, name, description, deadline, completed);
  }

  /**
   * TASKS -----------------------------------------------------------------------------------------
   */
  private final String tasksURI = "{projectId}/goals/{goalId}/tasks";

  @GetMapping(path = tasksURI)
  public List<Task> getTasks(@PathVariable("projectId") Long projectId,
                             @PathVariable("goalId") Long goalId) {
    return taskService.getTasks(projectId, goalId);
  }

  @GetMapping(path = tasksURI + "/{taskId}")
  public Optional<Task> getTask(@PathVariable("projectId") Long projectId,
                                @PathVariable("goalId") Long goalId,
                                @PathVariable("taskId") Long taskId) {
    return taskService.getTask(projectId, goalId, taskId);
  }

  @PostMapping(path = tasksURI)
  public void createNewTask(@PathVariable("projectId") Long projectId,
                            @PathVariable("goalId") Long goalId,
                            @RequestBody Task task) {
    taskService.addNewTask(projectId, goalId, task);
  }

  @DeleteMapping(path = tasksURI + "/{taskId}")
  public void deleteTask(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId,
                         @PathVariable("taskId") Long taskId) {
    taskService.deleteTask(projectId, goalId, taskId);
  }

  @PutMapping(path = tasksURI + "/{taskId}")
  public void updateTask(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long currentGoalId,
                         @PathVariable("taskId") Long taskId,
                         @RequestParam(required=false) String name,
                         @RequestParam(required=false) String description,
                         @RequestParam(required=false) Long goalId,
                         @RequestParam(required=false) Integer weight,
                         @RequestParam(required=false) Boolean completed) {
    taskService.updateTask(projectId, currentGoalId, taskId, name, description, goalId, weight, completed);
  }

}
