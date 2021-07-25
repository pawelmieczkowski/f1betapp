package pl.salata.f1betapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Team {

    @Id
    private Long id;
    private String name;
    private String nationality;
    private String url;
}
