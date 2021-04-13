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
  @Value("${service.group_service}")
  private String studentGroupService;

  @Autowired
  public ProjectService(ProjectRepository projectRepository, GoalRepository goalRepository, TaskRepository taskRepository) {
    this.projectRepository = projectRepository;
    this.goalRepository = goalRepository;
    this.taskRepository = taskRepository;
  }

  public List<Project> getProjects() {
    return projectRepository.findAll();
  }

  public Project getProject(Long projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    if (project.isEmpty()) {
      throw new IllegalStateException("Project with Id " + projectId + " does not exist.");
    } else {
      return project.get();
    }
  }

  public void createNewProject(Project project) {
    // throw an exception if not all required fields are filled in
    if (project.getStudentGroupID() == null || project.getName() == null ||
        project.getDescription() == null || project.getDeadline() == null) {
      throw new IllegalStateException("The following fields of the Project body cannot be null: studentGroupID, " +
          "name, description, deadline");
    }
    // throw an exception if an id is specified in the body of the POST request (they should be created by the server)
    if (project.getId() != null) {
      throw new IllegalStateException("Please remove the following fields from the body of the POST request: id");
      // The value of the DB id is redundant, since it is overwritten by the sequence_generator
    }
    // throw an exception if the studentGroupId of the to-be created project does not exist
    try {
      // the group id passed by the client to create the group
      Long clientGroupId = project.getStudentGroupID();
      // see if the client group id can be fetched from the StudentGroupService
      Long groupId = this.getStudentGroup(clientGroupId).getId();
    } catch (IllegalStateException e) {
      e.printStackTrace();
      throw new IllegalStateException("Invalid studentGroupID provided, studentGroup with that ID does not exist");
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
    String url = studentGroupService + "/api/group/" + studentGroupId;
    RestTemplate restTemplate = restTemplateBuilder.build();
    return restTemplate.getForObject(url, StudentGroup.class);
  }

}
