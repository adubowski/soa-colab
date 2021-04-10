package nl.utwente.soa.joinlink_service.services;


import java.util.List;
import java.util.Optional;
import nl.utwente.soa.joinlink_service.model.Meeting;
import nl.utwente.soa.joinlink_service.model.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public List<Meeting> getMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting getMeetingById(Long id) {
        Optional<Meeting> meeting = meetingRepository.findById(id);
        if (meeting.isPresent()) {
            return meeting.get();
        } else {
            throw new IllegalStateException("Meeting with id " + id + " does not exists");
        }
    }
}
