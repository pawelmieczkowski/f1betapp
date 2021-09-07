package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.repository.GrandPrixRepository;

import java.util.List;

@Service
public class GrandPrixService {

    private final GrandPrixRepository grandPrixRepository;

    public GrandPrixService(GrandPrixRepository grandPrixRepository) {
        this.grandPrixRepository = grandPrixRepository;
    }

    public GrandPrix getById(Long id) {
        return grandPrixRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GrandPrix.class, String.valueOf(id)));
    }

    public GrandPrix getByIdWithRaceResults(Long id) {
        return grandPrixRepository.findByIdAndFetchRaceResults(id)
                .orElseThrow(() -> new EntityNotFoundException(GrandPrix.class, String.valueOf(id)));
    }

    public GrandPrix getByIdWithQualificationResults(Long id) {
        return grandPrixRepository.findByIdAndFetchQualificationResults(id)
                .orElseThrow(() -> new EntityNotFoundException(GrandPrix.class, String.valueOf(id)));
    }

    public List<GrandPrix> getAllByYear(Integer year) {
        List<GrandPrix> grandPrix = grandPrixRepository.findAllByYear(year);
        if (!grandPrix.isEmpty()) {
            return grandPrix;
        } else {
            throw new EntityNotFoundException(GrandPrix.class, "YEAR = " + year);
        }
    }

    public List<Long> getAllYears() {
        return grandPrixRepository.findAllYears();
    }

    public List<GrandPrix> getByCircuitId(Long id) {
        List<GrandPrix> grandPrix = grandPrixRepository.findByCircuitId(id);
        if (!grandPrix.isEmpty()) {
            return grandPrix;
        } else {
            throw new EntityNotFoundException(GrandPrix.class, "CIRCUIT ID = " + id);
        }
    }

    public GrandPrix getByYearAndRound(Integer year, Integer round) {
        return grandPrixRepository.findByYearAndRound(year, round)
                .orElseThrow(() -> new EntityNotFoundException(GrandPrix.class, "YEAR = " + year + ", ROUND = " + round));
    }

    public GrandPrix save(GrandPrix grandPrix) {
        return grandPrixRepository.save(grandPrix);
    }
}
