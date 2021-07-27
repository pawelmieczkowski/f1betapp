package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.RaceResultService;

import java.util.List;

@RestController
@RequestMapping("race-result")
@AllArgsConstructor
public class RaceResultController {

    private final RaceResultService raceResultService;

    @GetMapping()
    public List<RaceResult> getByGrandPrixId(@RequestParam Long grandPrix) {
        return raceResultService.getByGrandPrixId(grandPrix);
    }
}
