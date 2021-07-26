package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.TeamService;

@AllArgsConstructor
public class QualificationResultDataProcessor implements ItemProcessor<QualificationResultInput, QualificationResult> {

    private final GrandPrixService grandPrixService;
    private final DriverService driverService;
    private final TeamService teamService;

    @Override
    public QualificationResult process(QualificationResultInput input) throws Exception {
        QualificationResult result = new QualificationResult();

        InputProcessor.parseNumber(input.getQualifyId(), Long.class).ifPresent(result::setId);
        InputProcessor.parseNumber(input.getPosition(), Integer.class).ifPresent(result::setResult);

        result.setQ1time(InputProcessor.validateString(input.getQ1()));
        result.setQ2time(InputProcessor.validateString(input.getQ2()));
        result.setQ3time(InputProcessor.validateString(input.getQ3()));
        result.setDriverNumber(InputProcessor.validateString(input.getNumber()));

        InputProcessor.parseNumber(input.getRaceId(), Long.class)
                .ifPresent(value -> {
                    GrandPrix grandPrix = grandPrixService.findById(value);
                    result.setGrandPrix(grandPrix);
                });

        InputProcessor.parseNumber(input.getDriverId(), Long.class)
                .ifPresent(value -> {
                    Driver driver = driverService.findById(value);
                    String driverName = driver.getForename() + " " + driver.getSurname();
                    result.setDriverName(driverName);
                });

        InputProcessor.parseNumber(input.getConstructorId(), Long.class)
                .ifPresent(value -> {
                    Team team = teamService.findById(value);
                    result.setTeamName(team.getName());
                });
        return result;
    }
}
