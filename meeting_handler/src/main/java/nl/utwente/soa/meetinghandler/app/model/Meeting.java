package nl.utwente.soa.meetinghandler.app.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
public class Meeting {
    @Id
    @SequenceGenerator(
            name="meeting_sequence",
            sequenceName = "meeting_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "meeting_sequence"
    )
    private Long id;

    @Getter
    @Setter
    private String joinLink;

    public Meeting() {
        this.joinLink = "www.dummy.join.link.nl";
    }
}
