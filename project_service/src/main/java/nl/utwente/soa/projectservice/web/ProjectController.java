package nl.utwente.soa.projectservice.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.utwente.soa.projectservice.model.Project;
import nl.utwente.soa.projectservice.services.ProjectService;
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
public class ProjectController {

  private final ProjectService projectService;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

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
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) Date deadline,
                            @RequestParam(required = false) Boolean completed) {
    projectService.updateProject(projectId, name, description, deadline, completed);
  }
}
