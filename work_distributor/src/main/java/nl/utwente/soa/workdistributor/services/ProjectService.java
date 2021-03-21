package nl.utwente.soa.workdistributor.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.workdistributor.model.Project;
import nl.utwente.soa.workdistributor.access.ProjectRepository;
import nl.utwente.soa.workdistributor.model.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;

  @Autowired
  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public List<Project> getProjects(){
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
    // throw an exception if the id of the project is already used
    if (project.getId() != null && projectRepository.existsById(project.getId())) {
      throw new IllegalStateException("Project ID taken");
    }
    // throw an exception if the name of the project is already used
//    Optional<Project> projectOptional = projectRepository.findProjectByName(project.getName());
//    if (projectOptional.isPresent()) {
//      throw new IllegalStateException("Name is already used");
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

  public void deleteProject(Long projectId) {
    boolean exists = projectRepository.existsById(projectId);
    if (!exists) {
      throw new IllegalStateException("Project with id " + projectId + " does not exist!");
    }
    projectRepository.deleteById(projectId);

  }

  @Transactional
  public void updateProject(Long projectId, String name, String description, Date deadline, Boolean completed) {
    Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException(
        "Project with id " + projectId + " does not exist!"
    ));

    if (name != null && name.length() > 0 && !Objects.equals(project.getName(), name)) {
//      Optional<Project> projectOptional = projectRepository.findProjectByName(name);
//      if (projectOptional.isPresent()) {
//        throw new IllegalStateException("This project name is already used!");
//      }
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
    String url = "http://localhost:8081/api/v1/group/" + studentGroupId;
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      StudentGroup studentGroup = restTemplate.getForObject(url, StudentGroup.class);
      return studentGroup;
    } catch (IllegalStateException e) {
      throw e;
    }
  }

}
