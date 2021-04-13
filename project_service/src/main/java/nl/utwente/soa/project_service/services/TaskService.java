package nl.utwente.soa.project_service.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.project_service.access.StudentTaskRepository;
import nl.utwente.soa.project_service.access.TaskRepository;
import nl.utwente.soa.project_service.model.Goal;
import nl.utwente.soa.project_service.model.Student;
import nl.utwente.soa.project_service.model.StudentTask;
import nl.utwente.soa.project_service.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final StudentTaskRepository studentTaskRepository;
  private final GoalService goalService;
  @Value("${service.group_service}")
  private String studentGroupService;

  @Autowired
  public TaskService(TaskRepository taskRepository, StudentTaskRepository studentTaskRepository, GoalService goalService) {
    this.taskRepository = taskRepository;
    this.studentTaskRepository = studentTaskRepository;
    this.goalService = goalService;
  }

  public List<Task> getTasks(Long projectId, Long goalId) {
    // throw an exception if the goal of the tasks does not exist
    try {
      Optional<Goal> goal = goalService.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    return taskRepository.findAllByProjectIdAndGoalId(projectId, goalId);
  }

  public Optional<Task> getTask(Long projectId, Long goalId, Long taskId) {
    // throw an exception if the goal of the task does not exist
    try {
      Optional<Goal> goal = goalService.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Task task = taskRepository.findTaskByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist within this goal within this project."
    ));
    return taskRepository.findTaskByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId);
  }

  public void addNewTask(Long projectId, Long goalId, Task task) {
    // throw an exception if not all required fields are filled in
    if (task.getName() == null || task.getDescription() == null || task.getWeight() == null) {
      throw new IllegalStateException("The following fields cannot be null: name, description, weight");
    }
    // throw an exception if an id is specified in the body of the POST request (they should be created by the server)
    if (task.getProjectId() != null || task.getGoalId() != null ||
        task.getTaskId() != null || task.getId() != null) {
      throw new IllegalStateException("Please remove the following fields from the body of " +
          "the POST request: projectId, goalId, taskId, id");
      // The value of the DB id actually doesn't matter, since it is overwritten by the sequence_generator
    }
    // throw an exception if either the project or the goal of the to-be-created task does not exist
    try {
      Optional<Goal> goal = goalService.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if the name of the task is already used within this goal
    List<Task> tasksWithGoalId = taskRepository.findAllByProjectIdAndGoalId(projectId, goalId);
    Long max = 0L;
    for (Task taskWithGoalId : tasksWithGoalId) {
      if (taskWithGoalId.getName().equals(task.getName())) {
        throw new IllegalStateException("Name of the task is already used within this goal");
      }
      if (taskWithGoalId.getTaskId() > max) {
        max = taskWithGoalId.getTaskId();
      }
    }
    // make sure these fields are set, because they are not specified in the body of the POST request
    task.setTaskId(max + 1);
    task.setGoalId(goalId);
    task.setProjectId(projectId);
    task.setCompleted(false);
    taskRepository.save(task);
  }

  @Transactional
  public void deleteTask(Long projectId, Long goalId, Long taskId) {
    // throw an exception if the goal of the task does not exist
    try {
      Optional<Goal> goal = goalService.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Task task = taskRepository.findTaskByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist within this goal within this project."
    ));
    taskRepository.deleteTaskByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId);
    // also delete every assignment of this task to a student
    studentTaskRepository.deleteAllByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId);
  }

  @Transactional
  public void deleteTasks(Long projectId, Long goalId) {
    // throw an exception if the goal of the tasks does not exist
    try {
      Optional<Goal> goal = goalService.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    taskRepository.deleteAllByProjectIdAndGoalId(projectId, goalId);
    // also delete every assignment of "all tasks of this goal" to a student
    studentTaskRepository.deleteAllByProjectIdAndGoalId(projectId, goalId);
  }

  @Transactional
  public void updateTask(Long projectId, Long goalId, Long taskId, String name, String description, Long goalIdChange, Integer weight, Boolean completed) {
    // throw an exception if the goal of the task does not exist
    try {
      Optional<Goal> goal = goalService.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Task task = taskRepository.findTaskByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist within this goal within this project."
    ));

    if (name != null && name.length() > 0 && !Objects.equals(task.getName(), name)) {
      // throw an exception if the name of the task is already used within this goal
      List<Task> tasksWithGoalId = taskRepository.findAllByProjectIdAndGoalId(projectId, goalId);
      for (Task taskWithGoalId : tasksWithGoalId) {
        if (taskWithGoalId.getName().equals(name)) {
          throw new IllegalStateException("This task name is already used in this goal!");
        }
      }
      task.setName(name);
    }

    if (description != null && description.length() > 0 && !Objects.equals(task.getDescription(), description)) {
      task.setDescription(description);
    }

    if (completed != null && !Objects.equals(task.getCompleted(), completed)) {
      task.setCompleted(completed);
    }

    if (weight != null && !Objects.equals(task.getWeight(), weight)) {
      task.setWeight(weight);
    }

    if (goalIdChange != null && !Objects.equals(goalId, goalIdChange)) {
      // throw an exception if the goalIdChange (so the new goal id) does not exist
      try {
        Optional<Goal> goal = goalService.getGoal(projectId, goalIdChange);
      } catch (IllegalStateException e) {
        throw e;
      }

      Goal oldGoal = goalService.getGoal(projectId, goalId).get();
      Goal newGoal = goalService.getGoal(projectId, goalIdChange).get();
      if (!Objects.equals(oldGoal.getProjectId(), newGoal.getProjectId())) {
        throw new IllegalStateException("You can only switch a task to another goal of the same project. " +
            "Switching a task to a goal of another project is not allowed.");
      }
      task.setGoalId(goalId);
    }


  }

  /*
  The following operations are about students assigned to a task
   */

  public List<Long> getStudentsOfTask(Long projectId, Long goalId, Long taskId) {
    // throw an exception if the task of the studentTasks does not exist
    Task task = this.getTask(projectId, goalId, taskId).get();
    List<StudentTask> studentTasks = studentTaskRepository.findAllByProjectIdAndGoalIdAndTaskId(projectId, goalId, taskId);
    List<Long> studentIds = new ArrayList<>();
    for(StudentTask studentTask : studentTasks) {
      studentIds.add(studentTask.getStudentId());
    }
    return studentIds;
  }

  public List<Long> getTasksOfStudent(Long projectId, Long studentId) {
    // throw an exception if the student of the studentTasks does not exist
    Student student = this.getStudent(studentId);
    List<StudentTask> studentTasks = studentTaskRepository.findAllByProjectIdAndStudentId(projectId, studentId);
    List<Long> taskIds = new ArrayList<>();
    for(StudentTask studentTask : studentTasks) {
      taskIds.add(studentTask.getTaskId());
    }
    return taskIds;
  }

  public void addStudentToTask(Long projectId, Long goalId, Long taskId, Long studentId) {
    Task task = this.getTask(projectId, goalId, taskId).get();
    Student student = this.getStudent(studentId);
    StudentTask studentTask = new StudentTask(studentId, taskId, projectId, goalId);
    studentTaskRepository.save(studentTask);
  }

  @Transactional
  public void deleteStudentFromTask(Long projectId, Long goalId, Long taskId, Long studentId) {
    Student student = this.getStudent(studentId);
    StudentTask studentTask = studentTaskRepository.findStudentTaskByProjectIdAndGoalIdAndStudentIdAndTaskId(
        projectId, goalId, studentId, taskId
    ).orElseThrow(() -> new IllegalStateException(
        "Student with Id " + studentId + " is not assigned to this task"
    ));
    studentTaskRepository.deleteStudentTaskByProjectIdAndGoalIdAndStudentIdAndTaskId(projectId, goalId, studentId, taskId);
  }

  // Get information from studentGroupService (Sync Communication)
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  public Student getStudent(Long studentId) {
    String url = studentGroupService + "api/students/" + studentId;
    RestTemplate restTemplate = restTemplateBuilder.build();
    try {
      Student student = restTemplate.getForObject(url, Student.class);
      return student;
    } catch (IllegalStateException e) {
      throw e;
    }
  }
}
