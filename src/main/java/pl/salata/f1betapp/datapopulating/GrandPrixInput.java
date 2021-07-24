package pl.salata.f1betapp.datapopulating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrandPrixInput {

    private String raceId;
    private String year;
    private String round;
    private String circuitId;
    private String name;
    private String date;
    private String time;
    private String url;
}
