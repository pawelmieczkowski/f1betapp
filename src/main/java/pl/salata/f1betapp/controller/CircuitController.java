package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.service.CircuitService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("circuits")
public class CircuitController {

    private final CircuitService circuitService;

    @GetMapping("{id}")
    public Circuit getCircuit(@PathVariable Long id){
        return circuitService.findById(id);
    }

    @GetMapping("all")
    public List<Circuit> getAllCircuits(){
        return circuitService.getAllCircuits();
    }

}
