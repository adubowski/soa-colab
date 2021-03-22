package nl.utwente.soa.meetinghandler.app.services;

import nl.utwente.soa.meetinghandler.app.model.Meeting;
import nl.utwente.soa.meetinghandler.app.model.MeetingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeetingConfig {

    @Bean
    CommandLineRunner meetingCLRunner(MeetingRepository repository) {
        return args -> {
            Meeting meeting = new Meeting();
            repository.save(meeting);
        };
    }
}
