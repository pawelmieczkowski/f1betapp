package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.repository.GrandPrixRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GrandPrixService {

    private final GrandPrixRepository grandPrixRepository;

    public GrandPrixService(GrandPrixRepository grandPrixRepository) {
        this.grandPrixRepository = grandPrixRepository;
    }

    public GrandPrix findById(Long id) {
        return grandPrixRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid id provided")));
    }

    public List<GrandPrix> findAllByYear(Integer year) {
        return grandPrixRepository.findAllByYear(year);
    }

}
