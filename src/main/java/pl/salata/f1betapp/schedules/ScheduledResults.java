package pl.salata.f1betapp.schedules;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.service.DataProcessingApiService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.RaceResultService;

import java.io.IOException;

@AllArgsConstructor
@Component
public class ScheduledResults {

    private final DataProcessingApiService dataProcessingApiService;
    private final RaceResultService raceResultService;
    private final GrandPrixService grandPrixService;

    @Scheduled(cron = "0 0 0 * * 1", zone = "Europe/Warsaw")
    public void executeTask() throws IOException {

        Long mostRecentGrandPrixId = raceResultService.getMostRecentResult().getGrandPrixId();
        GrandPrix recentGrandPrix = grandPrixService.getById(mostRecentGrandPrixId);
        Integer year = recentGrandPrix.getYear();
        Integer round = recentGrandPrix.getRound() + 1;

        dataProcessingApiService.populateRaceResult(year, round);
        dataProcessingApiService.populateQualificationResult(year, round);
        System.out.println(("xD"));
    }
}
