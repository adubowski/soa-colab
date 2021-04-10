package nl.utwente.soa.project_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class StudentGroup {

  private Long id;
  private String name;

  public StudentGroup() {
  }

  public StudentGroup(String name) {
    this.name = name;
  }

  public StudentGroup(Long id, String name) {
    this.id = id;
    this.name = name;
  }

}