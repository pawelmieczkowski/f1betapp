package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "qualification_result")
public class QualificationResult {

    @Id
    private Long id;
    @JoinColumn(name = "grand_prix_id", insertable = false, updatable = false)
    @ManyToOne
    private GrandPrix grandPrix;
    @Column(name = "grand_prix_id")
    private Long grandPrixId;
    private String driverNumber;
    private String driverName;
    private String teamName;
    private Integer result;
    private String q1time;
    private String q2time;
    private String q3time;
}
