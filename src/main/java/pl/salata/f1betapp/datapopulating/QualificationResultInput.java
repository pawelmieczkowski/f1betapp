package pl.salata.f1betapp.datapopulating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualificationResultInput {
    private String qualifyId;
    private String raceId;
    private String driverId;
    private String constructorId;
    private String number;
    private String position;
    private String q1;
    private String q2;
    private String q3;

}
