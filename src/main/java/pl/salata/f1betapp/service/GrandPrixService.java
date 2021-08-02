package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
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
                .orElseThrow(() -> new IllegalArgumentException(("Invalid id provided")));
    }

    public GrandPrix getByIdWithRaceResults(Long id) {
        return grandPrixRepository.findByIdAndFetchRaceResults(id)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid id provided")));
    }

    public GrandPrix getByIdWithQualificationResults(Long id) {
        return grandPrixRepository.findByIdAndFetchQualificationResults(id)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid id provided")));
    }

    public List<GrandPrix> getAllByYear(Integer year) {
        return grandPrixRepository.findAllByYear(year);
    }

    public List<Long> getAllYears() {
        return grandPrixRepository.findAllYears();
    }
}
