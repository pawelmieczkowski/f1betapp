package pl.salata.f1betapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.repository.RaceResultRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RaceResultService {

    private final RaceResultRepository raceResultRepository;

    public List<RaceResult> getByGrandPrixId(Long id) {
        List<RaceResult> raceResults = raceResultRepository.findAllByGrandPrixId(id);
        if (!raceResults.isEmpty()) {
            return raceResults;
        } else {
            throw new EntityNotFoundException(RaceResult.class, "GRAND PRIX ID = " + id);
        }
    }

    public List<String> getWinnerByGrandPrixId(Long id) {
        List<String> names = new ArrayList<>();
        List<RaceResult> results = raceResultRepository.findByGrandPrixIdAndFinishingPosition(id, "1");

        results.forEach((value) -> names.add(value.getDriverName()));

        if (!names.isEmpty()) {
            return names;
        } else {
            throw new EntityNotFoundException(String.class, "GRAND PRIX ID = " + id);
        }
    }

    public List<RaceResult> getDriverResults(Long id) {
        List<RaceResult> raceResults = raceResultRepository.findByDriver(id);
        if (!raceResults.isEmpty()) {
            return raceResults;
        } else {
            throw new EntityNotFoundException(RaceResult.class, String.valueOf(id));
        }
    }

    public List<RaceResult> getTeamResults(String name, Integer year) {
        List<RaceResult> raceResults = raceResultRepository.findByTeam(name, year);
        if (!raceResults.isEmpty()) {
            return raceResults;
        } else {
            throw new EntityNotFoundException(RaceResult.class, "TEAM = " + name + " YEAR = " + year);
        }
    }

    public List<Long> getAllYearsByTeam(String teamName) {
        List<Long> years = raceResultRepository.findAllYearsByTeam(teamName);
        if (!years.isEmpty()) {
            return years;
        } else {
            throw new EntityNotFoundException(Long.class, String.valueOf(teamName));
        }
    }

    public List<RaceResult> saveAll(List<RaceResult> raceResult) {
        return raceResultRepository.saveAll(raceResult);
    }

    public RaceResult getMostRecentResult(){
        return raceResultRepository.findTopByOrderByIdDesc();
    }
}
