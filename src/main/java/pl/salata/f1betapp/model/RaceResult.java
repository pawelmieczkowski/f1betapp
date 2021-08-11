package pl.salata.f1betapp.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@JsonFilter("RaceResultFilter")
@JsonIgnoreProperties(  {"handler","hibernateLazyInitializer"} )
@Entity(name = "race_result")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class RaceResult {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="grand_prix_id")
    private GrandPrix grandPrix;
    @Column(name="grand_prix_id", insertable = false, updatable = false)
    private long grandPrixId;
    private Long driverId;
    private String driverNumber;
    private String driverName;
    private String teamName;
    private Integer startingGridPosition;
    private String finishingPosition;
    private Float points;
    private String laps;
    private String time;
    private Integer timeInMilliseconds;
    private String fastestLapTime;
    private String fastestLapSpeed;
    private String status;
    private Integer ranking;
}