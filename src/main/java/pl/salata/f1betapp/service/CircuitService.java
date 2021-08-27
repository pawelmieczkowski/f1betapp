package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.repository.CircuitRepository;

import java.util.List;

@Service
public class CircuitService {

    private final CircuitRepository circuitRepository;

    public CircuitService(CircuitRepository circuitRepository) {
        this.circuitRepository = circuitRepository;
    }

    public Circuit findById(Long id) {
        return circuitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Circuit.class, String.valueOf(id)));
    }

    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }
}
