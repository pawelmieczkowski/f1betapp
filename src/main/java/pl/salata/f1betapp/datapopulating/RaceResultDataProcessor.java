package pl.salata.f1betapp.datapopulating;

import io.swagger.models.auth.In;
import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.*;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.RaceFinishStatusService;
import pl.salata.f1betapp.service.TeamService;


public class RaceResultDataProcessor implements ItemProcessor<RaceResultInput, RaceResult> {

    private final RaceFinishStatusService statusService;
    private final TeamService teamService;
    private final DriverService driverService;
    private final GrandPrixService grandPrixService;

    public RaceResultDataProcessor(RaceFinishStatusService statusService,
                                   TeamService teamService,
                                   DriverService driverService,
                                   GrandPrixService grandPrixService) {
        this.statusService = statusService;
        this.teamService = teamService;
        this.driverService = driverService;
        this.grandPrixService = grandPrixService;
    }

    @Override
    public RaceResult process(RaceResultInput input) throws Exception {
        RaceResult raceResult = new RaceResult();

        raceResult.setFinishingPosition(InputProcessor.validateString(input.getPositionText()));
        raceResult.setLaps(InputProcessor.validateString(input.getLaps()));
        raceResult.setTeamName(InputProcessor.validateString(input.getTime()));
        raceResult.setFastestLapTime(InputProcessor.validateString(input.getFastestLapTime()));
        raceResult.setFastestLapSpeed(InputProcessor.validateString(input.getFastestLapSpeed()));

        InputProcessor.parseNumber(input.getResultId(), Long.class).ifPresent(raceResult::setId);
        InputProcessor.parseNumber(input.getGrid(), Integer.class).ifPresent(raceResult::setStartingGridPosition);
        InputProcessor.parseNumber(input.getPoints(), Float.class).ifPresent(raceResult::setPoints);
        InputProcessor.parseNumber(input.getMilliseconds(), Integer.class).ifPresent(raceResult::setTimeInMilliseconds);
        InputProcessor.parseNumber(input.getRank(), Integer.class).ifPresent(raceResult::setRanking);

        InputProcessor.parseNumber(input.getStatusId(), Long.class)
                .ifPresent(value -> {
                    RaceFinishStatus status = statusService.findById(value);
                    raceResult.setStatus(status.getStatus());
                });

        InputProcessor.parseNumber(input.getConstructorId(), Long.class)
                .ifPresent(value -> {
                    Team team = teamService.findById(value);
                    raceResult.setTeamName(team.getName());
                });

        InputProcessor.parseNumber(input.getDriverId(), Long.class)
                .ifPresent(value -> {
                    Driver driver = driverService.findById(value);
                    String driverName = driver.getForename() + " " + driver.getSurname();
                    raceResult.setDriverName(driverName);
                    raceResult.setDriverNumber(driver.getDriverNumber());
                });

        InputProcessor.parseNumber(input.getRaceId(), Long.class)
                .ifPresent(value -> {
                    GrandPrix grandPrix = grandPrixService.findById(value);
                    raceResult.setGrandPrix(grandPrix);
                });

        return raceResult;
    }


}
