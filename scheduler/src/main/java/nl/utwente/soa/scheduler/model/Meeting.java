package nl.utwente.soa.scheduler.model;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meeting {

  private Long id;
  private Boolean planned;
  private Date date;
  private Goal goal;
  private List<Task> tasks;



}
