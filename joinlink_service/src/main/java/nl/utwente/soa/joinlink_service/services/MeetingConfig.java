package nl.utwente.soa.joinlink_service.services;

import nl.utwente.soa.joinlink_service.model.Meeting;
import nl.utwente.soa.joinlink_service.model.MeetingRepository;
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
