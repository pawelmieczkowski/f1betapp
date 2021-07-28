package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity(name = "race_result")
public class RaceResult {

    @Id
    private Long id;
    @Column(name = "grand_prix_id")
    private Long grandPrixId;
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