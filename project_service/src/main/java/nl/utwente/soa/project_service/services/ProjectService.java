package nl.utwente.soa.project_service.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.project_service.access.GoalRepository;
import nl.utwente.soa.project_service.access.ProjectRepository;
import nl.utwente.soa.project_service.access.TaskRepository;
import nl.utwente.soa.project_service.model.Project;
import nl.utwente.soa.project_service.model.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final GoalRepository goalRepository;
  private final TaskRepository taskRepository;
  @Value("${studentgroup.port}")
  private String studentGroupPort;

  @Autowired
  public ProjectService(ProjectRepository projectRepository, GoalRepository goalRepository, TaskRepository taskRepository) {
    this.projectRepository = projectRepository;
    this.goalRepository = goalRepository;
    this.taskRepository = taskRepository;
  }

  public List<Project> getProjects() {
    return projectRepository.findAll();
  }

  public Optional<Project> getProject(Long projectId) {
    if (!projectRepository.existsById(projectId)) {
      throw new IllegalStateException("Project with Id " + projectId + " does not exist.");
    } else {
      return projectRepository.findById(projectId);
    }
  }

  public void createNewProject(Project project) {
    // The value of the DB id doesn't matter, since it is overwritten by the sequence_generator
    // throw an exception if the id of the project is already used
//    if (project.getId() != null && projectRepository.existsById(project.getId())) {
//      throw new IllegalStateException("DataBase Project ID taken");
//    }
    // throw an exception if the studentGroupId of the to-be created project does not exist
    try {
      // the group id passed by the client to create the group
      Long clientGroupId = project.getStudentGroupID();
      // see if the client group id can be fetched from the StudentGroupService
      Long groupId = this.getStudentGroup(clientGroupId).getId();
    } catch (IllegalStateException e) {
      throw e;
    }
    projectRepository.save(project);
  }

  @Transactional
  public void deleteProject(Long projectId) {
    boolean exists = projectRepository.existsById(projectId);
    if (!exists) {
      throw new IllegalStateException("Project with id " + projectId + " does not exist!");
    }
    projectRepository.deleteById(projectId);
    // also delete all goals of this project
    goalRepository.deleteAllByProjectId(projectId);
    // also delete all tasks of this project
    taskRepository.deleteAllByProjectId(projectId);
  }

  @Transactional
  public void updateProject(Long projectId, String name, String description, Date deadline, Boolean completed) {
    Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException(
        "Project with id " + projectId + " does not exist!"
    ));

    if (name != null && name.length() > 0 && !Objects.equals(project.getName(), name)) {
      project.setName(name);
    }

    if (description != null && description.length() > 0 && !Objects.equals(project.getDescription(), description)) {
      project.setDescription(description);
    }

    if (deadline != null && !Objects.equals(project.getDeadline(), deadline)) {
      project.setDeadline(deadline);
    }

    if (completed != null && !Objects.equals(project.getCompleted(), completed)) {
      project.setCompleted(completed);
    }
  }

  // Get information from studentGroupService (Sync Communication)
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  public StudentGroup getStudentGroup(Long studentGroupId) {
    String url = "http://localhost:" + studentGroupPort + "/api/v1/group/" + studentGroupId;
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      StudentGroup studentGroup = restTemplate.getForObject(url, StudentGroup.class);
      return studentGroup;
    } catch (IllegalStateException e) {
      throw e;
    }
  }

}
