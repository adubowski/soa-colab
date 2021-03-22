package nl.utwente.soa.workdistributor.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.utwente.soa.workdistributor.model.Goal;
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
@RequestMapping(path = "api/v1/projects/{projectId}/goals")
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
  public Optional<Goal> getGoal(@PathVariable("projectId") Long projectId,
                                @PathVariable("goalId") Long goalId) {
    return goalService.getGoal(projectId, goalId);
  }

  @PostMapping
  public void createNewGoal(@PathVariable("projectId") Long projectId,
                            @RequestBody Goal goal) {
    goalService.addNewGoal(projectId, goal);
  }

  @DeleteMapping("{goalId}")
  public void deleteGoal(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId) {
    goalService.deleteGoal(projectId, goalId);
  }

  @PutMapping("{goalId}")
  public void updateGoal(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId,
                         @RequestParam(required=false) String name,
                         @RequestParam(required=false) String description,
                         @RequestParam(required=false) Date deadline,
                         @RequestParam(required=false) Boolean completed) {
    goalService.updateGoal(projectId, goalId, name, description, deadline, completed);
  }
}
