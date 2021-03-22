package nl.utwente.soa.meetinghandler.app.services;


import nl.utwente.soa.meetinghandler.app.model.Meeting;
import nl.utwente.soa.meetinghandler.app.model.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
