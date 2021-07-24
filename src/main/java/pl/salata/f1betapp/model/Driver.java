package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Driver {

    @Id
    private Long id;
    private String driverNumber;
    private String driverCode;
    private String forename;
    private String surname;
    private LocalDate dateOfBirth;
    private String nationality;
    private String url;
}
