package nl.utwente.soa.workdistributor.web;

import java.util.Date;
import java.util.List;
import javax.persistence.PreRemove;
import nl.utwente.soa.workdistributor.model.Goal;
import nl.utwente.soa.workdistributor.services.GoalService;
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
@RequestMapping(path = "api/v1/goal")
public class GoalController {

  private final GoalService goalService;

  // autowired, because @Service GoalService should be instantiated
  @Autowired
  public GoalController(GoalService goalService) {
    this.goalService = goalService;
  }

  @GetMapping
  public List<Goal> getGoals(){
    return goalService.getGoals();
  }

  @PostMapping
  public void createNewGoal(@RequestBody Goal goal) {
    goalService.addNewGoal(goal);
  }

  @DeleteMapping(path = "{goalId}")
  public void deleteGoal(@PathVariable("goalId") Long goalId) {
    goalService.deleteStudent(goalId);
  }

  @PutMapping(path = "{goalId}")
  public void updateGoal(@PathVariable("goalId") Long goalId,
                         @RequestParam(required=false) String name,
                         @RequestParam(required=false) String description,
                         @RequestParam(required=false) Date deadline,
                         @RequestParam(required=false) boolean completed) {
    goalService.updateGoal(goalId, name, description, deadline, completed);
  }

}
