package pl.salata.f1betapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.repository.RaceResultRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RaceResultService {

    private final RaceResultRepository raceResultRepository;

    public List<RaceResult> getByGrandPrixId(Long id) {
        return raceResultRepository.findAllByGrandPrixId(id);
    }

    public List<String> getWinnerByGrandPrixId(Long id) {
        List<String> names = new ArrayList<>();
        List<RaceResult> results = raceResultRepository.findByGrandPrixIdAndFinishingPosition(id, "1");

        results.forEach((value) -> names.add(value.getDriverName()));
        return names;
    }
}
