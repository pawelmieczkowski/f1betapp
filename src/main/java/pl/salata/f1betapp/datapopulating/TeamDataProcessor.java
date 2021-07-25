package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.model.RaceFinishStatus;

public class TeamDataProcessor implements ItemProcessor<TeamInput, Team> {

    @Override
    public Team process(TeamInput input) throws Exception {
        Team team = new Team();

        InputProcessor.parseNumber(input.getConstructorId(), Long.class).ifPresent(team::setId);

        team.setName(InputProcessor.validateString(input.getName()));
        team.setNationality(InputProcessor.validateString(input.getNationality()));
        team.setUrl(InputProcessor.validateString(input.getUrl()));

        return team;
    }
}
