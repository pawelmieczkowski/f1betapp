package pl.salata.f1betapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.service.CircuitService;

@RestController
public class CircuitController {

    private final CircuitService circuitService;

    public CircuitController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    @GetMapping("/circuit{id}")
    public Circuit getCircuit(@PathVariable String id){
        return circuitService.findById(id);
    }
}
