package nl.utwente.soa.joinlink_service.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
