package nl.utwente.soa.meetinghandler.app.rest;

import nl.utwente.soa.meetinghandler.app.model.Meeting;
import nl.utwente.soa.meetinghandler.app.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/meetings")
public class MeetingRestController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingRestController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public List<Meeting> getMeetings() {
        return meetingService.getMeetings();
    }

    @GetMapping(path="{meetingId}")
    public Meeting getMeetingById(@PathVariable("meetingId") Long id) {
        return meetingService.getMeetingById(id);
    }
}
