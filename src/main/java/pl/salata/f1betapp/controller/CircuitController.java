package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.service.CircuitService;

@RestController
@AllArgsConstructor
public class CircuitController {

    private final CircuitService circuitService;

    @GetMapping("/circuit{id}")
    public Circuit getCircuit(@PathVariable Long id){
        return circuitService.findById(id);
    }
}
