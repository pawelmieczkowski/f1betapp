package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity(name = "grand_prix")
public class GrandPrix {

    @Id
    private Long id;
    private Integer year;
    private Integer round;
    @ManyToOne
    @JoinColumn(name ="circuit_id")
    private Circuit circuit;
    private String name;
    private LocalDate date;
    private String localization;
    private String country;
    private LocalTime time;
    private String url;

}
