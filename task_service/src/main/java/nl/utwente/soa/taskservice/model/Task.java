package nl.utwente.soa.taskservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Task {
  @Id
  @SequenceGenerator(
      name = "task_sequence",
      sequenceName = "task_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "task_sequence"
  )
  private Long id;
  private String name;
  private String description;
  private Long goalId;
  private Integer weight;
  private Boolean completed;

  public Task() {
  }

  public Task(String name, String description, Long goalId, Integer weight, Boolean completed) {
    this.name = name;
    this.description = description;
    this.goalId = goalId;
    this.weight = weight;
    this.completed = completed;
  }

  public Task(Long id, String name, String description, Long goalId, Integer weight, Boolean completed) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.goalId = goalId;
    this.weight = weight;
    this.completed = completed;
  }


}
