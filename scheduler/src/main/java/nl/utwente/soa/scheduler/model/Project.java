package nl.utwente.soa.scheduler.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Project {
  private Long id;
  private Long studentGroupID;
  private String name;
  private String description;
  private Date deadline;
  private Boolean completed;

  public Project() {
  }

  public Project(Long studentGroupID, String name, String description, Date deadline, Boolean completed) {
    this.studentGroupID = studentGroupID;
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.completed = completed;
  }

//  public Project(Long id, Long studentGroupID, String name, String description, Date deadline, Boolean completed) {
//    this.id = id;
//    this.studentGroupID = studentGroupID;
//    this.name = name;
//    this.description = description;
//    this.deadline = deadline;
//    this.completed = completed;
//  }
}
