package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.RaceFinishStatus;
import pl.salata.f1betapp.repository.RaceFinishStatusRepository;

@Service
public class RaceFinishStatusService {

    private final RaceFinishStatusRepository raceFinishStatusRepository;

    public RaceFinishStatusService(RaceFinishStatusRepository raceFinishStatusRepository) {
        this.raceFinishStatusRepository = raceFinishStatusRepository;
    }

    public RaceFinishStatus findById(Long id) {
        return raceFinishStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RaceFinishStatus.class, String.valueOf(id)));
    }
}
