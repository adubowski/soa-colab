package nl.utwente.soa.meeting_service.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import nl.utwente.soa.meeting_service.access.MeetingRepository;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class MeetingConfig {

    @Autowired
    MeetingService meetingService;

    @Bean
    CommandLineRunner meetingCLRunner(MeetingRepository repository) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        final Date tomorrow = dt;
        return args -> {
            Meeting meeting = new Meeting(1L, 1L, 1L, tomorrow);
            if (repository.findMeetingByProjectIdAndGoalIdAndMeetingId(
                    meeting.getMeetingId(),
                    meeting.getProjectId(),
                    meeting.getGoalId()
            ).isEmpty()) {
                Long projectId = meeting.getProjectId();
                Long goalId = meeting.getGoalId();
                meeting.setProjectId(null);
                meeting.setGoalId(null);
                meeting.setMeetingId(null);
                meetingService.createNewMeeting(projectId, goalId, meeting);
            }
        };
    }
}
