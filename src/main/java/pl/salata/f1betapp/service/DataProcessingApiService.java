package pl.salata.f1betapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.salata.f1betapp.datapopulating.api.QualifyingResultDataService;
import pl.salata.f1betapp.datapopulating.api.RaceResultDataService;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class DataProcessingApiService {

    RaceResultDataService raceResultDataService;
    QualifyingResultDataService qualifyingResultDataService;
    RaceResultService raceResultService;
    QualificationResultService qualificationResultService;
    GrandPrixService grandPrixService;

    public List<RaceResult> populateRaceResult(Integer year, Integer round) throws IOException {
        GrandPrix grandPrix = grandPrixService.getByYearAndRound(year, round);
        List<RaceResult> results = raceResultDataService.populateRaceResult(year, round, grandPrix.getId());
        results = raceResultService.saveAll(results);

        grandPrix.setDriverName(findWinner(results));
        grandPrixService.save(grandPrix);
        return results;
    }

    public List<QualificationResult> populateQualificationResult(Integer year, Integer round) throws IOException {
        GrandPrix grandPrix = grandPrixService.getByYearAndRound(year, round);
        List<QualificationResult> results = qualifyingResultDataService.populateRaceResult(year, round, grandPrix.getId());
        return qualificationResultService.saveAll(results);
    }

    private String findWinner(List<RaceResult> results) {
        String winner = null;
        for (RaceResult res : results) {
            if (res.getFinishingPosition().equals("1"))
                winner = res.getDriverName();
        }
        return winner;
    }
}
