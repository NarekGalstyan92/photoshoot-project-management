package am.itspace.photoshootprojectmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;

    private String avatarUrl;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active;

    private String emailToken;

    private boolean hasLeftReview;

    private boolean isDeleted;

}
