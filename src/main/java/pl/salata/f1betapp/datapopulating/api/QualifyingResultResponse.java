package pl.salata.f1betapp.datapopulating.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualifyingResultResponse {

    private String number;
    private String position;
    private String driverGivenName;
    private String driverFamilyName;
    private String driverDateOfBirth;
    private String constructorName;
    private String q1time;
    private String q2time;
    private String q3time;
}
