package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "qualification_result")
public class QualificationResult {

    @Id
    @GeneratedValue(generator="myGenerator")
    @GenericGenerator(name="myGenerator", strategy="pl.salata.f1betapp.model.UseExistingOrGenerateIdGenerator")
    @Column(unique=true, nullable=false)
    private Long id;
    @Column(name = "grand_prix_id")
    private Long grandPrixId;
    private String driverNumber;
    private Long driverId;
    private String driverName;
    private String teamName;
    private Integer result;
    private String q1time;
    private String q2time;
    private String q3time;
}
