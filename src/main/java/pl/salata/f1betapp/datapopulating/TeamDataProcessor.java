package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.model.RaceFinishStatus;

public class TeamDataProcessor implements ItemProcessor<TeamInput, Team> {

    @Override
    public Team process(TeamInput input) throws Exception {
        Team team = new Team();

        team.setId(Long.parseLong(input.getConstructorId()));
        team.setName(input.getName());
        team.setNationality(input.getNationality());
        team.setUrl(input.getUrl());
        return team;
    }
}
