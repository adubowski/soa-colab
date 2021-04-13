package nl.utwente.soa.front_end_service.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.utwente.soa.front_end_service.model.Goal;
import nl.utwente.soa.front_end_service.model.Project;
import nl.utwente.soa.front_end_service.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FrontEndService {

  @Value("${service.project_service}")
  private String projectService;

  @Autowired
  public FrontEndService() {
  }

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  /*
  PROJECT_SERVICE: Project
   */

  public Project getProject(Long projectId) {
    String url = projectService + "/api/projects/" + projectId;
    RestTemplate restTemplate = restTemplateBuilder.build();
    try {
      Project project = restTemplate.getForObject(url, Project.class);
      return project;
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  public List<Project> getProjects() {
    String url = projectService + "/api/projects";
    RestTemplate restTemplate = restTemplateBuilder.build();
    try {
      Project[] projects = restTemplate.getForObject(url, Project[].class);
      return new ArrayList<>(Arrays.asList(projects));
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  /*
  PROJECT_SERVICE: Goal
   */

  public List<Goal> getGoalsOfProject(Long projectId) {
    String url = projectService + "/api/projects/" + projectId + "/goals";
    RestTemplate restTemplate = restTemplateBuilder.build();
    try {
      Goal[] goals = restTemplate.getForObject(url, Goal[].class);
      return new ArrayList<>(Arrays.asList(goals));
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  public void addGoal(Long projectId, Goal goal) {
    String url = projectService + "/api/projects/" + projectId + "/goals";
    RestTemplate restTemplate = restTemplateBuilder.build();
    try {
      HttpEntity<Goal> request = new HttpEntity<>(goal);
      restTemplate.postForObject(url, request, Goal.class);
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  /*
  PROJECT_SERVICE: Task
   */

  public List<Task> getTasksOfGoal(Long projectId, Long goalId) {
    String url = projectService + "/api/projects/" + projectId + "/goals/" + goalId + "/tasks";
    RestTemplate restTemplate = restTemplateBuilder.build();
    try {
      Task[] tasks = restTemplate.getForObject(url, Task[].class);
      return new ArrayList<>(Arrays.asList(tasks));
    } catch (IllegalStateException e) {
      throw e;
    }
  }
}
