package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.repository.GrandPrixRepository;

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
}
