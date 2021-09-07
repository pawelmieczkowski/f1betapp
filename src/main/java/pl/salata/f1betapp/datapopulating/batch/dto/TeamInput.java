package pl.salata.f1betapp.datapopulating.batch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInput {

    private String constructorId;
    private String constructorRef;
    private String name;
    private String nationality;
    private String url;
}
