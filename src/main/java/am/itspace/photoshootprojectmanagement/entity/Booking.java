package am.itspace.photoshootprojectmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    private String address;

    private int participants;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date eventStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date eventEndTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private EventCategory eventCategory;

    @ManyToOne
    private User user;

}
