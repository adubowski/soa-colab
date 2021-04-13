package nl.utwente.soa.project_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Student {

  private Long id;
  private String name;
  private String email;
  private Long groupId;

  public Student() {
  }

  public Student(String name, String email, Long groupId) {
    this.name = name;
    this.email = email;
    this.groupId = groupId;
  }


}
