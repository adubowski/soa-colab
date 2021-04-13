package nl.utwente.soa.project_service.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.utwente.soa.project_service.model.Goal;
import nl.utwente.soa.project_service.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping(path = "api/projects/{projectId}/goals")
public class GoalController {

  private final GoalService goalService;

  @Autowired
  public GoalController(GoalService goalService) {
    this.goalService = goalService;
  }

  @GetMapping
  public List<Goal> getGoals(@PathVariable("projectId") Long projectId) {
    return goalService.getGoals(projectId);
  }

  @GetMapping("{goalId}")
  public Goal getGoal(@PathVariable("projectId") Long projectId, @PathVariable("goalId") Long goalId) {
    return goalService.getGoal(projectId, goalId);
  }

  @PostMapping
  public void createNewGoal(@PathVariable("projectId") Long projectId, @RequestBody Goal goal) {
    goalService.addNewGoal(projectId, goal);
  }

  @DeleteMapping("{goalId}")
  public void deleteGoal(@PathVariable("projectId") Long projectId, @PathVariable("goalId") Long goalId) {
    goalService.deleteGoal(projectId, goalId);
  }

  @DeleteMapping
  public void deleteGoals(@PathVariable("projectId") Long projectId) {
    goalService.deleteGoals(projectId);
  }

  @PutMapping("{goalId}")
  public void updateGoal(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
                         @RequestParam(required = false) Boolean completed) {
    goalService.updateGoal(projectId, goalId, name, description, deadline, completed);
  }
}
