package nl.utwente.soa.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SchedulerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SchedulerApplication.class, args);
  }

}
