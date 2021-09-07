package pl.salata.f1betapp.datapopulating.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceResultsResponse {

    private String number;
    private String position;
    private String positionText;
    private String points;
    private String driverGivenName;
    private String driverFamilyName;
    private String driverDateOfBirth;
    private String constructorName;
    private String grid;
    private String laps;
    private String status;
    private String timeInMilliseconds;
    private String time;
    private String fastestLapRank;
    private String fastestLapLap;
    private String fastestLapTime;
    private String fastestLapSpeed;
}
