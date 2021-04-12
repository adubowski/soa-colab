package nl.utwente.soa.meeting_service.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.model.Task;
import nl.utwente.soa.meeting_service.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/projects/{projectId}/goals/{goalId}/meetings")
public class MeetingController {

  private final MeetingService meetingService;

  @Autowired
  public MeetingController(MeetingService meetingService) {
    this.meetingService = meetingService;
  }

  @GetMapping
  public List<Meeting> getMeetings(@PathVariable("projectId") Long projectId,
                                   @PathVariable("goalId") Long goalId) {
    return meetingService.getMeetings(projectId, goalId);
  }

  @GetMapping("{meetingId}")
  public Meeting getMeeting(@PathVariable("projectId") Long projectId,
                                      @PathVariable("goalId") Long goalId,
                                      @PathVariable("meetingId") Long meetingId) {
    return meetingService.getMeeting(projectId, goalId, meetingId);
  }

  @PostMapping
  public void createNewMeeting(@PathVariable("projectId") Long projectId,
                               @PathVariable("goalId") Long goalId,
                               @RequestBody Meeting meeting) {
    meetingService.createNewMeeting(projectId, goalId, meeting);
  }

  @DeleteMapping("{meetingId}")
  public void deleteMeeting(@PathVariable("projectId") Long projectId,
                         @PathVariable("goalId") Long goalId,
                         @PathVariable("meetingId") Long meetingId) {
    meetingService.deleteMeeting(projectId, goalId, meetingId);
  }

  @PutMapping("{meetingId}")
  public void rescheduleMeeting(@PathVariable("projectId") Long projectId,
                          @PathVariable("goalId") Long goalId,
                          @PathVariable("meetingId") Long meetingId,
                          @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
    meetingService.rescheduleMeeting(projectId, goalId, meetingId, date);
  }

  /*
  The following operations are about tasks DISCUSSED at a meeting
   */

  @GetMapping("{meetingId}/tasks")
  public List<Long> getTasksOfMeeting(@PathVariable("projectId") Long projectId,
                                      @PathVariable("goalId") Long goalId,
                                      @PathVariable("meetingId") Long meetingId) {
    return meetingService.getTasksOfMeeting(projectId, goalId, meetingId);
  }

  @PostMapping("{meetingId}/tasks")
  public void addTaskToMeeting(@PathVariable("projectId") Long projectId,
                               @PathVariable("goalId")Long goalId,
                               @PathVariable("meetingId") Long meetingId,
                               @RequestBody Long taskId) {
    meetingService.addTaskToMeeting(projectId, goalId, meetingId, taskId);
  }

  @DeleteMapping("{meetingId}/tasks/{taskId}")
  public void deleteTaskFromMeeting(@PathVariable("projectId") Long projectId,
                            @PathVariable("goalId") Long goalId,
                            @PathVariable("meetingId") Long meetingId,
                            @PathVariable("taskId") Long taskId) {
    meetingService.deleteTaskFromMeeting(projectId, goalId, meetingId, taskId);
  }

}
