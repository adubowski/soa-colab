package nl.utwente.soa.front_end_service.web;

import javax.validation.Valid;
import nl.utwente.soa.front_end_service.model.Goal;
import nl.utwente.soa.front_end_service.services.FrontEndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontEndController {

  private final FrontEndService frontEndService;

  private final String projectPath = "api/projects";

  @Autowired
  public FrontEndController(FrontEndService frontEndService) {
    this.frontEndService = frontEndService;
  }

  @GetMapping(projectPath)
  public String overviewProjects(Model model) {
    model.addAttribute("projects", frontEndService.getProjects());
    return "overviewProjects";
  }

  @GetMapping(projectPath + "/{projectId}/goals")
  public String overviewGoals(@PathVariable("projectId") Long projectId, Model model) {
    model.addAttribute("goals", frontEndService.getGoalsOfProject(projectId));
    return "overviewGoals";
  }

  @GetMapping(projectPath + "/{projectId}/addGoal")
  public String showFormAddGoal(@PathVariable("projectId") Long projectId, Model model, Goal goal) {
    model.addAttribute("project", frontEndService.getProject(projectId));
    return "addGoal";
  }

  @PostMapping(projectPath + "/{projectId}/goals")
  public String addGoal(@PathVariable("projectId") Long projectId, @Valid Goal goal, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "addGoal";
    }
    frontEndService.addGoal(projectId, goal);
    return "overviewGoals";
  }


  @GetMapping(projectPath + "/{projectId}/goals/{goalId}/tasks")
  public String overviewGoals(@PathVariable("projectId") Long projectId,
                              @PathVariable("goalId") Long goalId,
                              Model model) {
    model.addAttribute("tasks", frontEndService.getTasksOfGoal(projectId, goalId));
    return "overviewTasks";
  }
}
