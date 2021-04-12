package nl.utwente.soa.meeting_service.web;

import java.util.Date;
import java.util.List;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/unplannedmeetings")
public class SchedulerController {

  private final SchedulerService schedulerService;

  @Autowired
  public SchedulerController(SchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  // get a list of unplanned meetings
  @GetMapping
  public List<Meeting> getUnplannedMeetings() {
    return schedulerService.getUnplannedMeetings();
  }

  // get a single unplanned meeting
  @GetMapping("{meetingId}")
  public Meeting getUnplannedMeeting(@PathVariable("meetingId") Long meetingId) {
    return schedulerService.getUnplannedMeeting(meetingId);
  }

  // create a meeting from an unplanned meeting
  @PutMapping("{meetingId}")
  public void createMeetingFromUnplannedMeeting(@PathVariable("meetingId") Long meetingId,
                                                @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
    schedulerService.createMeetingFromUnplannedMeeting(meetingId, date);

  }


}
