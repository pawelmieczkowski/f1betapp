package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.repository.CircuitRepository;

@Service
public class CircuitService {

    private final CircuitRepository circuitRepository;

    public CircuitService(CircuitRepository circuitRepository) {
        this.circuitRepository = circuitRepository;
    }

    public Circuit findById(String id){
        return circuitRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id Provided"));
    }
}
