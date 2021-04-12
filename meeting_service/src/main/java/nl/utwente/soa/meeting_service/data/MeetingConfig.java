package nl.utwente.soa.meeting_service.data;

import java.util.Date;
import nl.utwente.soa.meeting_service.access.MeetingRepository;
import nl.utwente.soa.meeting_service.model.Meeting;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeetingConfig {

    @Bean
    CommandLineRunner meetingCLRunner(MeetingRepository repository) {
        return args -> {
            Meeting meeting = new Meeting(1L, 1L, 1L, new Date(), null);
            repository.save(meeting);
        };
    }
}
