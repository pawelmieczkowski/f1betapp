package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class QualificationResult {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "grand_prix_id")
    private GrandPrix grandPrix;
    private String driverNumber;
    private String driverName;
    private String teamName;
    private Integer result;
    private String q1time;
    private String q2time;
    private String q3time;
}
