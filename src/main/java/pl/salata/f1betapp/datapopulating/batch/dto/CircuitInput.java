package pl.salata.f1betapp.datapopulating.batch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircuitInput {

    private String circuitId;
    private String circuitRef;
    private String name;
    private String location;
    private String country;
    private String latitude;
    private String longitude;
    private String altitude;
    private String url;
}
