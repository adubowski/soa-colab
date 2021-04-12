package nl.utwente.soa.group_service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
/**
 * This class represents the Student that is a MEMBER of a studentGroup.
 * Student-StudentGroup is a many-to-many relationship, so this Member object also represents the
 * relationship table for this Student-StudentGroup relationship.
 */
public class Member {

  @Id
  @SequenceGenerator(
      name="member_sequence",
      sequenceName = "member_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "member_sequence"
  )
  private Long id;
  private Long studentId;
  private Long studentGroupId;

  public Member() {
  }

  public Member(Long studentId, Long studentGroupId) {
    this.studentId = studentId;
    this.studentGroupId = studentGroupId;
  }

}
