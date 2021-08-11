package pl.salata.f1betapp.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@JsonFilter("GrandPrixFilter")
@JsonIgnoreProperties(  {"handler","hibernateLazyInitializer"} )
@Entity(name = "grand_prix")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GrandPrix {

    @Id
    private Long id;
    private Integer year;
    private Integer round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circuit_id")
    private Circuit circuit;
    private String name;
    private LocalDate date;
    private String localization;
    private String country;
    private LocalTime time;
    private String url;
    private String driverName;

    @OneToMany(mappedBy = "grandPrix")
    private List<RaceResult> raceResult;

    @OneToMany
    @JoinColumn(name = "grand_prix_id")
    private List<QualificationResult> qualificationResult;
}
